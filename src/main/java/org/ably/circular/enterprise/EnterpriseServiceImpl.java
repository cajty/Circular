package org.ably.circular.enterprise;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ably.circular.exception.BusinessException;
import org.ably.circular.exception.NotFoundException;
import org.ably.circular.location.LocationRepository;
import org.ably.circular.role.AppRole;
import org.ably.circular.role.RoleService;
import org.ably.circular.security.CurrentUserProvider;
import org.ably.circular.user.User;
import org.ably.circular.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EnterpriseServiceImpl implements EnterpriseService {


    private final EnterpriseRepository enterpriseRepository;
    private final EnterpriseMapper enterpriseMapper;
    private final CurrentUserProvider currentUserProvider;
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

private List<String> validateEnterpriseError(EnterpriseRequest request) {
    List<String> errors = new ArrayList<>();

    // Check for duplicate registration number
    if (request.getRegistrationNumber() != null) {

        if (enterpriseRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
            errors.add("Enterprise with registration number " + request.getRegistrationNumber() + " already exists");
        }
    }





    // Validate enterprise name
    if (request.getName() == null || request.getName().trim().isEmpty()) {
        errors.add("Enterprise name is required");
    } else if (request.getName().length() < 3 || request.getName().length() > 100) {
        errors.add("Enterprise name must be between 3 and 100 characters");
    }

    return errors;
}

private void validateEnterpriseRequest(EnterpriseRequest request) {
    if (request == null) {
        throw new IllegalArgumentException("Enterprise request cannot be null");
    }

    List<String> errors = validateEnterpriseError(request);
    if (!errors.isEmpty()) {
        throw new BusinessException(String.join(", ", errors), HttpStatus.BAD_REQUEST);
    }
}

private List<String> validateVerificationStatusUpdateError(VerificationStatusUpdateRequest request) {
    List<String> errors = new ArrayList<>();

    // Check if enterprise exists
    if (request.getEnterpriseId() == null) {
        errors.add("Enterprise ID is required");
    } else {
        try {
            existsById(request.getEnterpriseId());
        } catch (NotFoundException e) {
            errors.add("Enterprise not found with ID: " + request.getEnterpriseId());
        }
    }

    // Validate new status
    if (request.getNewStatus() == null) {
        errors.add("New status is required");
    } else {
        try {
            VerificationStatus.valueOf(request.getNewStatus().name());
        } catch (IllegalArgumentException e) {
            errors.add("Invalid verification status: " + request.getNewStatus());
        }
    }

    // Validate status transition
    if (request.getEnterpriseId() != null && request.getNewStatus() != null) {
        try {
            Enterprise enterprise = findEntityById(request.getEnterpriseId());
            VerificationStatus currentStatus = enterprise.getStatus();

            if (!isValidStatusTransition(currentStatus, request.getNewStatus())) {
                errors.add("Invalid status transition from " + currentStatus + " to " + request.getNewStatus());
            }

            // If status is being rejected, reason is required
            if (request.getNewStatus() == VerificationStatus.REJECTED &&
                (request.getReason() == null || request.getReason().trim().isEmpty())) {
                errors.add("Rejection reason is required when setting status to REJECTED");
            }
        } catch (NotFoundException e) {
            // Enterprise not found, already handled above
        }
    }

    return errors;
}

private boolean isValidStatusTransition(VerificationStatus currentStatus, VerificationStatus newStatus) {
    if (currentStatus == newStatus) {
        return true; // No change is always valid
    }

    switch (currentStatus) {
        case PENDING:
            return newStatus == VerificationStatus.UNDER_REVIEW || newStatus == VerificationStatus.REJECTED;
        case UNDER_REVIEW:
            return newStatus == VerificationStatus.VERIFIED || newStatus == VerificationStatus.REJECTED;
        case VERIFIED:
            return newStatus == VerificationStatus.REJECTED; // Can revoke verification
        case REJECTED:
            return newStatus == VerificationStatus.PENDING; // Can reapply
        default:
            return false;
    }
}

private void validateVerificationStatusUpdateRequest(VerificationStatusUpdateRequest request) {
    if (request == null) {
        throw new IllegalArgumentException("Verification status update request cannot be null");
    }

    List<String> errors = validateVerificationStatusUpdateError(request);
    if (!errors.isEmpty()) {
        throw new BusinessException(String.join(", ", errors), HttpStatus.BAD_REQUEST);
    }
}


    @Override
    @Transactional
    public EnterpriseResponse save(Enterprise enterprise) {
        Enterprise savedEnterprise = enterpriseRepository.save(enterprise);
        return enterpriseMapper.toResponse(savedEnterprise);
    }


    @Override
    @Transactional
    public EnterpriseResponse create(EnterpriseRequest request) {
        validateEnterpriseRequest(request);
        Enterprise enterprise = enterpriseMapper.toEntity(request);
        enterprise.setStatus(VerificationStatus.PENDING);

        UUID useId = currentUserProvider.getCurrentUserIdOrThrow();

        enterprise.setVerifiedBy(useId);
        return save(enterprise);

    }


    @Override
    @Transactional
    public EnterpriseResponse update(Long id, EnterpriseRequest request) {
        Enterprise existingEnterprise = findEntityById(id);
        validateEnterpriseRequest(request);

        enterpriseMapper.updateEntityFromRequest(request, existingEnterprise);

        Enterprise updatedEnterprise = enterpriseRepository.save(existingEnterprise);
        return enterpriseMapper.toResponse(updatedEnterprise);
    }


    @Override
    @Transactional
    public void delete(Long id) {
        if (!enterpriseRepository.existsById(id)) {
            throw new NotFoundException("Enterprise", id);
        }
        enterpriseRepository.deleteById(id);
    }


    @Override
    public EnterpriseResponse findById(Long id) {
        Enterprise enterprise = findEntityById(id);
        return enterpriseMapper.toResponse(enterprise);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<EnterpriseResponse> findAll(Pageable pageable) {
        return enterpriseRepository.findAll(pageable)
                .map(enterpriseMapper::toResponse);
    }


    @Override
    @Transactional(readOnly = true)
    public void existsById(Long id) {
        if (!enterpriseRepository.existsById(id)) {
            throw new NotFoundException("Enterprise", id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Enterprise findEntityById(Long id) {
        return enterpriseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Enterprise", id));
    }

    @Override
    @Transactional
    public EnterpriseResponse getEnterpriseOfUser() {
        return enterpriseMapper.toResponse(
                currentUserProvider
                .getCurrentUserEnterpriseOrThrow());
     }

    @Override
    @Transactional
    public void updateVerificationStatus(VerificationStatusUpdateRequest request) {
         Enterprise enterprise = findEntityById(request.getEnterpriseId());
         enterprise.setStatus(request.getNewStatus());
         enterprise.setRejectionReason(request.getReason());

         if(request.getNewStatus() == VerificationStatus.VERIFIED){
             User user = currentUserProvider.getCurrentUserOrThrow();
             user.setRoles(
                     roleService.getRolesByName("MANAGER")
             );
             user.setEnterprise(enterprise);
         }
    }


}

