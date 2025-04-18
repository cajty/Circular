package org.ably.circular.MaterialCategory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ably.circular.exception.DuplicateEntityException;
import org.ably.circular.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

     private void validateCategoryRequest(CategoryRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Category request cannot be null");
        }
      if (categoryRepository.existsByName(
              request.getName().toLowerCase()
      )) {
            throw new DuplicateEntityException("Category","name",request.getName());
      }
     }

    @Override
    @Transactional(readOnly = true)
    public void existsById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException("Category", id);
        }
    }

     private Category findEntityById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category", id));
    }

    @Override
    public CategoryResponse save(Category category) {
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(savedCategory);
    }

    @Override
    public CategoryResponse create(CategoryRequest request) {
        validateCategoryRequest(request);
        Category category = categoryMapper.toEntity(request);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(savedCategory);
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest request) {
         validateCategoryRequest(request);
        Category existingCategory = findEntityById(id);
        categoryMapper.updateEntityFromRequest(request, existingCategory);
        Category updatedCategory = categoryRepository.save(existingCategory);
        return categoryMapper.toResponse(updatedCategory);
    }

    @Override
    public void delete(Long id) {
        existsById(id);
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id) {
        Category category = findEntityById(id);
        return categoryMapper.toResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponse> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toResponse);
    }


    @Override
    public void changeActivityStatus(Long id) {
         existsById(id);
       try {
        categoryRepository.changeActivityStatus(id);
    } catch (Exception e) {
        throw new IllegalStateException("Failed to update activity status for City with ID " + id, e);
    }
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Category> findAllActiveCategory() {
        return categoryRepository.findAllByIsActiveTrue();
    }


}

