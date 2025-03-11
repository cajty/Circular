package org.ably.circular.location;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface LocationMapper {

    @Mapping(target = "cityName", source = "city.name")
    LocationResponse toResponse(Location location);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "materials", ignore = true)
    @Mapping(target = "city.id", source = "cityId")
    Location toEntity(LocationRequest request);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "materials", ignore = true)
    void updateEntityFromRequest(LocationRequest request, @MappingTarget Location location);
}