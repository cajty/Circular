package org.ably.circular.location;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper for Location entity and DTOs
 * Uses Spring component model for automatic dependency injection
 */
@Mapper(componentModel = "spring")
public interface LocationMapper {

    /**
     * Convert Location entity to LocationResponse
     */
    LocationResponse toResponse(Location location);

    /**
     * Convert LocationRequest to Location entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "materials", ignore = true)
    Location toEntity(LocationRequest request);

    /**
     * Update existing Location entity with LocationRequest data
     * Preserves the existing id and relationships
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "materials", ignore = true)
    void updateEntityFromRequest(LocationRequest request, @MappingTarget Location location);
}