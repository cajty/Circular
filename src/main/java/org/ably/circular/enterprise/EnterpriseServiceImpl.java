package org.ably.circular.enterprise;

import lombok.RequiredArgsConstructor;
import org.ably.circular.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of EnterpriseService following SOLID principles:
 *
 * S (Single Responsibility): Each method handles one specific task
 * O (Open/Closed): Extended through composition and new implementations
 * L (Liskov Substitution): Follows contract defined by EnterpriseService interface
 * I (Interface Segregation): Uses focused interface without unnecessary methods
 * D (Dependency Inversion): Depends on abstractions (interfaces) not concrete implementations
 */
@Service
@RequiredArgsConstructor // Constructor injection - follows Dependency Inversion Principle
@Transactional // Transaction management for data consistency
public class EnterpriseServiceImpl implements EnterpriseService {

    // Final fields with constructor injection - Better for testing and loose coupling
    private final EnterpriseRepository enterpriseRepository;
    private final EnterpriseMapper enterpriseMapper; // We'll need to create this

    /**
     * Single Responsibility Principle: Method only handles saving existing enterprise
     */
    @Override
    @Transactional
    public EnterpriseResponse save(Enterprise enterprise) {
        Enterprise savedEnterprise = enterpriseRepository.save(enterprise);
        return enterpriseMapper.toResponse(savedEnterprise);
    }

    /**
     * Single Responsibility Principle: Method only handles creating new enterprise
     * Open/Closed Principle: New validation rules can be added without modifying existing code
     */
    @Override
    @Transactional
    public EnterpriseResponse create(EnterpriseRequest request) {
        validateEnterpriseRequest(request);
        Enterprise enterprise = enterpriseMapper.toEntity(request);
        enterprise.setStatus(VerificationStatus.PENDING); // Business logic
        Enterprise savedEnterprise = enterpriseRepository.save(enterprise);
        return enterpriseMapper.toResponse(savedEnterprise);
    }

    /**
     * Single Responsibility Principle: Method only handles updating existing enterprise
     * Liskov Substitution Principle: Maintains the contract defined in interface
     */
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

    /**
     * Single Responsibility Principle: Method only handles deletion
     */
    @Override
    @Transactional
    public void delete(Long id) {
        if (!enterpriseRepository.existsById(id)) {
            throw new NotFoundException("Enterprise", id);
        }
        enterpriseRepository.deleteById(id);
    }

    /**
     * Single Responsibility Principle: Method only handles finding by ID
     * Open/Closed Principle: Error handling can be extended without modifying method
     */
    @Override
    @Transactional(readOnly = true) // Optimization for read operations
    public EnterpriseResponse findById(Long id) {
        Enterprise enterprise = findEntityById(id);
        return enterpriseMapper.toResponse(enterprise);
    }

    /**
     * Single Responsibility Principle: Method only handles pagination
     * Open/Closed Principle: Sorting and filtering can be added without modification
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EnterpriseResponse> findAll(Pageable pageable) {
        return enterpriseRepository.findAll(pageable)
                .map(enterpriseMapper::toResponse);
    }

    /**
     * Single Responsibility Principle: Method only checks existence
     */
    @Override
    @Transactional(readOnly = true)
    public void existsById(Long id) {
        if (!enterpriseRepository.existsById(id)) {
            throw new NotFoundException("Enterprise", id);
        }
    }

    /**
     * Private helper method for finding entity
     * DRY Principle: Reuse common functionality
     */
    private Enterprise findEntityById(Long id) {
        return enterpriseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Enterprise", id));
    }

    /**
     * Private validation method
     * Single Responsibility Principle: Handles only validation logic
     */
    private void validateEnterpriseRequest(EnterpriseRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Enterprise request cannot be null");
        }
        //   // to continu
    }
}

