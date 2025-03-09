package org.ably.circular.enterprise;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface EnterpriseMapper {


    EnterpriseResponse toResponse(Enterprise enterprise);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "verifiedAt", ignore = true)
    @Mapping(target = "users", ignore = true)
    Enterprise toEntity(EnterpriseRequest request);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "verifiedAt", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "locations", ignore = true)
    void updateEntityFromRequest(EnterpriseRequest request, @MappingTarget Enterprise enterprise);
}