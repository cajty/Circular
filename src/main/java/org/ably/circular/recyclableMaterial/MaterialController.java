package org.ably.circular.recyclableMaterial;

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

@RestController
@RequestMapping("/material")
@AllArgsConstructor
@Tag(name = "Material Controller", description = "Material Management APIs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@PreAuthorize("hasRole('MANAGER')")
public class MaterialController {

    private final MaterialService materialService;

    @Operation(summary = "Create a new material")
    @PostMapping
    public ResponseEntity<MaterialResponse> create(@Valid @RequestBody MaterialRequest materialRequest) {
        MaterialResponse response = materialService.create(materialRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a material by ID")
    @GetMapping("/{id}")
    public ResponseEntity<MaterialResponse> getById(@PathVariable Long id) {
        MaterialResponse response = materialService.findById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Update an existing material")
    @PutMapping("/{id}")
    public ResponseEntity<MaterialResponse> update(@PathVariable Long id,@Valid @RequestBody MaterialRequest materialRequest) {
        MaterialResponse response = materialService.update(id, materialRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Delete a material by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        materialService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Get all materials with pagination")
    @GetMapping
    public ResponseEntity<Page<MaterialResponse>> getAll(Pageable pageable) {
        Page<MaterialResponse> response = materialService.findAll(pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

     @Operation(summary = "Search materials with filters")
    @GetMapping("/search")
    public ResponseEntity<Page<MaterialResponse>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Float minPrice,
            @RequestParam(required = false) Float maxPrice,
            @RequestParam(required = false) MaterialStatus status,
            Pageable pageable) {
        Page<MaterialResponse> response = materialService.search(name, minPrice, maxPrice, status, pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}