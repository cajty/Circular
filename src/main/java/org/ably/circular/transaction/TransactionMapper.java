package org.ably.circular.transaction;

import org.ably.circular.enterprise.Enterprise;
import org.ably.circular.enterprise.EnterpriseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper for Transaction entity and DTOs
 * Uses Spring component model for automatic dependency injection
 */
@Mapper(componentModel = "spring", uses = {TransactionMapper.class})
public interface TransactionMapper {

    /**
     * Convert Transaction entity to TransactionResponse
     * Uses EnterpriseMapper to convert buyer and seller entities
     */
    TransactionResponse toResponse(Transaction transaction);

    /**
     * Convert TransactionRequest to Transaction entity
     * Sets initial status as PENDING and createdAt to current date
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "createdAt", expression = "java(new java.util.Date())")
    @Mapping(target = "completedAt", ignore = true)
    @Mapping(target = "buyer", source = "buyerId")
    @Mapping(target = "seller", source = "sellerId")
    Transaction toEntity(TransactionRequest request);

    /**
     * Update existing Transaction entity with TransactionRequest data
     * Preserves the existing id, status, dates, and relationships
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    @Mapping(target = "buyer", source = "buyerId")
    @Mapping(target = "seller", source = "sellerId")
    void updateEntityFromRequest(TransactionRequest request, @MappingTarget Transaction transaction);

    /**
     * Convert buyer/seller ID to Enterprise entity
     * This method needs to be implemented in the implementing class
     */
    default Enterprise longToEnterprise(Long id) {
        if (id == null) {
            return null;
        }
        Enterprise enterprise = new Enterprise();
        enterprise.setId(id);
        return enterprise;
    }
}