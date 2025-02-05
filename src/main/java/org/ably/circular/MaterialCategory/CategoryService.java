package org.ably.circular.MaterialCategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    CategoryResponse save(Category category);

    CategoryResponse create(CategoryRequest categoryRequest);

    CategoryResponse update(Long id, CategoryRequest categoryRequest);

    void delete(Long id);

    CategoryResponse findById(Long id);

    Page<CategoryResponse> findAll(Pageable pageable);

    void existsById(Long id);
}