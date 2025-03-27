package org.ably.circular.MaterialCategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface CategoryService {
    void existsById(Long id);
    CategoryResponse save(Category category);

    CategoryResponse create(CategoryRequest categoryRequest);

    CategoryResponse update(Long id, CategoryRequest categoryRequest);

    void delete(Long id);

    CategoryResponse findById(Long id);

    Page<CategoryResponse> findAll(Pageable pageable);

   void changeActivityStatus(Long id);

     Set<Category> findAllActiveCategory();


}