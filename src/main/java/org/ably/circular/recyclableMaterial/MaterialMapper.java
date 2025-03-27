package org.ably.circular.recyclableMaterial;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MaterialMapper {
    @Mapping(target = "location", source = "location")
    @Mapping(target = "category", source = "category")
    MaterialResponse toResponse(Material material);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category.id", source = "categoryId")
    @Mapping(target = "location.id", source = "locationId")
    Material toEntity(MaterialRequest request);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(MaterialRequest request, @MappingTarget Material material);
}