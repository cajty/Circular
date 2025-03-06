package org.ably.circular.enterprise;

import lombok.RequiredArgsConstructor;
import org.ably.circular.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor // Constructor injection - follows Dependency Inversion Principle
@Transactional // Transaction management for data consistency
public class EnterpriseServiceImpl implements EnterpriseService {


    private final EnterpriseRepository enterpriseRepository;
    private final EnterpriseMapper enterpriseMapper; // We'll need to create this

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
        enterprise.setStatus(VerificationStatus.PENDING); // Business logic
        Enterprise savedEnterprise = enterpriseRepository.save(enterprise);
        return enterpriseMapper.toResponse(savedEnterprise);
    }


    @Override
    @Transactional
    public EnterpriseResponse update(Long id, EnterpriseRequest request) {
        Enterprise existingEnterprise = findEntityById(id);
        validateEnterpriseRequest(request);

        // Partial update pattern - only update non-null fields
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
    @Transactional(readOnly = true) // Optimization for read operations
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


    private Enterprise findEntityById(Long id) {
        return enterpriseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Enterprise", id));
    }



}

