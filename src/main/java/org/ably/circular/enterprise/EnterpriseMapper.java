package org.ably.circular.enterprise;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper for Enterprise entity and DTOs
 * Uses Spring component model for automatic dependency injection
 */
@Mapper(componentModel = "spring")
public interface EnterpriseMapper {

    /**
     * Convert Enterprise entity to EnterpriseResponse
     */
    EnterpriseResponse toResponse(Enterprise enterprise);

    /**
     * Convert EnterpriseRequest to Enterprise entity
     * Sets initial status as PENDING
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "verifiedAt", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "locations", ignore = true)
    Enterprise toEntity(EnterpriseRequest request);

    /**
     * Update existing Enterprise entity with EnterpriseRequest data
     * Preserves the existing id, status, and relationships
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "verifiedAt", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "locations", ignore = true)
    void updateEntityFromRequest(EnterpriseRequest request, @MappingTarget Enterprise enterprise);
}