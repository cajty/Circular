package org.ably.circular.MaterialCategory;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface CategoryMapper {




    CategoryResponse toResponse(Category category);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", expression = "java(request.getName().toLowerCase())")
    @Mapping(target = "createdAt", expression = "java(new java.util.Date())")
    @Mapping(target = "isActive", expression = "java(request.getIsActive() != null ? request.getIsActive() : true)")
    Category toEntity(CategoryRequest request);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", expression = "java(request.getName().toLowerCase())")
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromRequest(CategoryRequest request, @MappingTarget Category category);
}