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


    LocationResponse toResponse(Location location);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "materials", ignore = true)
    Location toEntity(LocationRequest request);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "materials", ignore = true)
    void updateEntityFromRequest(LocationRequest request, @MappingTarget Location location);
}