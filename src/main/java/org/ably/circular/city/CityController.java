package org.ably.circular.city;

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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cities")
@AllArgsConstructor
@Tag(name = "City Controller", description = "City Management APIs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CityController {

    private final CityService cityService;

    @Operation(summary = "Create a new city")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CityResponse> create(@Valid @RequestBody CityRequest cityRequest) {
        CityResponse response = cityService.create(cityRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a city by ID")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CityResponse> getById(@PathVariable Long id) {
        CityResponse response = cityService.findById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Update an existing city")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CityResponse> update(@PathVariable Long id, @RequestBody CityRequest cityRequest) {
        CityResponse response = cityService.update(id, cityRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Delete a city by ID")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cityService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Get all cities")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<Set<CityResponse>> getAll() {
    Set<CityResponse> response = cityService.findAll();
    return new ResponseEntity<>(response, HttpStatus.OK);
}

    @Operation(summary = "Change the status of a city by ID")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/status/{id}")
    public ResponseEntity<Void> changeStatus(@PathVariable Long id) {
        cityService.changeActivityStatus(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
     @Operation(summary = "Get all active cities")
     @PreAuthorize("hasRole('MANAGER')")
     @GetMapping("/active")
     public ResponseEntity<Set<ActiveCityResponse>> getAllActiveCities() {
         Set<ActiveCityResponse> response = cityService.getAllActiveCities();
         return new ResponseEntity<>(response, HttpStatus.OK);
     }
}