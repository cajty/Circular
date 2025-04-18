package org.ably.circular.location;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface LocationMapper {

    @Mapping(target = "cityName", source = "city.name")
    @Mapping(target = "isActive", source = "isActive")
    @Mapping(target = "type", source = "type")
    LocationResponse toResponse(Location location);


    @Mapping(target = "id", source = "id")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "cityName", source = "city.name")
    ActiveLocationResponse toActiveLocationResponse(Location location);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "materials", ignore = true)
    @Mapping(target = "city.id", source = "cityId")
    Location toEntity(LocationRequest request);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "materials", ignore = true)
    void updateEntityFromRequest(LocationRequest request, @MappingTarget Location location);
}