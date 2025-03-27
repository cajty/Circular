package org.ably.circular.city;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface CityMapper {


    CityResponse toResponse(City city);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ActiveCityResponse toActiveCityResponse(City city);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", expression = "java(request.getName().toLowerCase())")
    @Mapping(target = "createdAt", expression = "java(new java.util.Date())")
    @Mapping(target = "isActive", expression = "java(request.getIsActive() != null ? request.getIsActive() : true)")
    City toEntity(CityRequest request);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", expression = "java(request.getName().toLowerCase())")
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromRequest(CityRequest request, @MappingTarget City city);
}