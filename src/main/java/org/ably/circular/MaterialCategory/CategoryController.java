package org.ably.circular.MaterialCategory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/categories")
@AllArgsConstructor
@Tag(name = "Category Controller", description = "Category Management APIs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Create a new category")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest categoryRequest) {
        CategoryResponse response = categoryService.create(categoryRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a category by ID")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable Long id) {
        CategoryResponse response = categoryService.findById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Update an existing category")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest) {
        CategoryResponse response = categoryService.update(id, categoryRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Delete a category by ID")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Get all categories with pagination")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<CategoryResponse>> getAll(Pageable pageable) {
        Page<CategoryResponse> response = categoryService.findAll(pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Change that status of category by Id")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/status/{id}")
    public ResponseEntity<Void> changeStatus(@PathVariable Long id){
        categoryService.changeActivityStatus(id);
       return new  ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "get all active category")
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/active-category")
    public ResponseEntity<Set<Category>> getAllActiveCategory(){
        Set<Category> response = categoryService.findAllActiveCategory();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}