package org.ably.circular.location;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/location")
@Tag(name = "Location Controller", description = "Location Management APIs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
public class LocationController {
    private  final LocationService locationService;

    @Operation(summary = "Creat a new location")
    @PostMapping
    public ResponseEntity<LocationResponse> create(@RequestBody LocationRequest request){
        LocationResponse response = locationService.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get an location by id")
    @GetMapping("/{id}")
    public ResponseEntity<LocationResponse> getById(@PathVariable Long id){
        LocationResponse response = locationService.findById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "delete an location by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        locationService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT) ;
    }

   @Operation(summary = "Update an existing location")
   @PutMapping("/{id}")
   public ResponseEntity<LocationResponse> update(@PathVariable Long id, @RequestBody LocationRequest request) {
       LocationResponse response = locationService.update(id, request);
       return new ResponseEntity<>(response, HttpStatus.OK);
   }


   @Operation(summary = "get all that location of enterprise")
   @GetMapping("/enterprise")
   public ResponseEntity<Page<LocationResponse>> getAllLocationOfEnterprise(Pageable pageable) {
      Page<LocationResponse> response = locationService.getAllLocationOfEnterprise(pageable);
      return new ResponseEntity<>(response, HttpStatus.OK);
  }

   @Operation(summary = "get active location ")
   @GetMapping("/enterprise-active")
   public ResponseEntity<Set<ActiveLocationResponse>> getAllActiveLocations() {
       Set<ActiveLocationResponse> response = locationService.getAllActiveLocationsOfEnterprise();
       return new ResponseEntity<>(response, HttpStatus.OK);
   }


   @Operation(summary = "Change activity status of a location")
   @PatchMapping("/{id}/status")
   public ResponseEntity<Void> changeActivityStatus(@PathVariable Long id) {
       locationService.changeActivityStatus(id);
       return new ResponseEntity<>(HttpStatus.NO_CONTENT);
   }

}
