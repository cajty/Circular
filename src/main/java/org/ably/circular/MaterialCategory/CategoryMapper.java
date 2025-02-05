package org.ably.circular.MaterialCategory;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper for Category entity and DTOs
 * Uses Spring component model for automatic dependency injection
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper {

    /**
     * Convert Category entity to CategoryResponse
     */
    CategoryResponse toResponse(Category category);

    /**
     * Convert CategoryRequest to Category entity
     * Sets initial createdAt to current date and isActive to true if not provided
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(new java.util.Date())")
    @Mapping(target = "isActive", expression = "java(request.getIsActive() != null ? request.getIsActive() : true)")
    Category toEntity(CategoryRequest request);

    /**
     * Update existing Category entity with CategoryRequest data
     * Preserves the existing id and createdAt
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromRequest(CategoryRequest request, @MappingTarget Category category);
}