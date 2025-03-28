package org.ably.circular.location;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface LocationService {

    LocationResponse save(Location location);

    LocationResponse create(LocationRequest location);

    LocationResponse update(Long id, LocationRequest location);

    void delete(Long id);

    LocationResponse findById(Long id);

    Page<LocationResponse> findAll(Pageable pageable);

    void existsById(Long id);

    Page<LocationResponse> getAllLocationOfEnterprise(Pageable pageable);

    Set<ActiveLocationResponse> getAllActiveLocationsOfEnterprise();



    void changeActivityStatus(Long id);
}