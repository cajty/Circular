package org.ably.circular.recyclableMaterial;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MaterialMapper {

    MaterialResponse toResponse(Material material);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    Material toEntity(MaterialRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateEntityFromRequest(MaterialRequest request, @MappingTarget Material material);
}