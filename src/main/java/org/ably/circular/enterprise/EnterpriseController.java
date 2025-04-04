package org.ably.circular.enterprise;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enterprise")
@AllArgsConstructor
@Tag(name = "Enterprise Controller", description = "Enterprise Management APIs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EnterpriseController {

    private final EnterpriseService enterpriseService;

    @Operation(summary = "Create a new enterprise")
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EnterpriseResponse> create(@Valid @RequestBody EnterpriseRequest enterpriseRequest) {
        EnterpriseResponse response = enterpriseService.create(enterpriseRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get an enterprise by ID")
    @GetMapping("/{id}")
    public ResponseEntity<EnterpriseResponse> getById(@PathVariable Long id) {
        EnterpriseResponse response = enterpriseService.findById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Operation(summary = "Update verification status")
    @PostMapping("/status")
   @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateVerificationStatus(
            @Valid @RequestBody VerificationStatusUpdateRequest request) {

        enterpriseService.updateVerificationStatus(request);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Update an existing enterprise")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<EnterpriseResponse> update(@PathVariable Long id, @Valid @RequestBody EnterpriseRequest enterpriseRequest) {
        EnterpriseResponse response = enterpriseService.update(id, enterpriseRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Delete an enterprise by ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        enterpriseService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
     @GetMapping("/user")
     @PreAuthorize("hasAnyRole('MANAGER','USER')")
     public ResponseEntity<EnterpriseResponse> getEnterpriseOfUser() {
         EnterpriseResponse response = enterpriseService.getEnterpriseOfUser();
         return new ResponseEntity<>(response, HttpStatus.OK);
     }


    @Operation(summary = "Get all enterprises with pagination")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<EnterpriseResponse>> getAll(Pageable pageable) {
        Page<EnterpriseResponse> response = enterpriseService.findAll(pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}