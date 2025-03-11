package org.ably.circular.location;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
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

    @Operation(summary = "get active location ")
   @GetMapping("/active")
   public ResponseEntity<Set<ActiveLocationResponse>> getAllActiveLocations() {
       Set<ActiveLocationResponse> response = locationService.getAllActiveLocations();
       return new ResponseEntity<>(response, HttpStatus.OK);
   }

   @Operation(summary = "get all that location of enterprise")
   @GetMapping("/enterprise")
   public ResponseEntity<Set<ActiveLocationResponse>> getAllLocationOfEnterprise() {
       Set<ActiveLocationResponse> response = locationService.getAllLocationOfEnterprise();
       return new ResponseEntity<>(response, HttpStatus.OK);
   }

}
