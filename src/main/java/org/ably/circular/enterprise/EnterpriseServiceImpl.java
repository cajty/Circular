package org.ably.circular.enterprise;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ably.circular.exception.NotFoundException;
import org.ably.circular.security.CurrentUserProvider;
import org.ably.circular.user.User;
import org.ably.circular.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EnterpriseServiceImpl implements EnterpriseService {


    private final EnterpriseRepository enterpriseRepository;
    private final EnterpriseMapper enterpriseMapper;
    private final CurrentUserProvider currentUserProvider;



     private void validateEnterpriseRequest(EnterpriseRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Enterprise request cannot be null");
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



}

