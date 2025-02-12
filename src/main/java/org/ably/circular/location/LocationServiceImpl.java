package org.ably.circular.location;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ably.circular.exception.NotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

     private void validateLocationRequest(LocationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Location request cannot be null");
        }
        // Additional validation logic
    }

    @Override
    @Transactional
    public LocationResponse save(Location location) {
        Location savedLocation = locationRepository.save(location);
        return locationMapper.toResponse(savedLocation);
    }

    @Override
    @Transactional
    public LocationResponse create(LocationRequest request) {
        validateLocationRequest(request);
        Location location = locationMapper.toEntity(request);
        Location savedLocation = locationRepository.save(location);
        return locationMapper.toResponse(savedLocation);
    }

    @Override
    @Transactional
    public LocationResponse update(Long id, LocationRequest request) {
        Location existingLocation = findEntityById(id);
        validateLocationRequest(request);
        locationMapper.updateEntityFromRequest(request, existingLocation);
        Location updatedLocation = locationRepository.save(existingLocation);
        return locationMapper.toResponse(updatedLocation);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!locationRepository.existsById(id)) {
            throw  new NotFoundException("Location", id);
        }
        locationRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public LocationResponse findById(Long id) {
        Location location = findEntityById(id);
        return locationMapper.toResponse(location);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LocationResponse> findAll(Pageable pageable) {
        return locationRepository.findAll(pageable)
                .map(locationMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public void existsById(Long id) {
        if (!locationRepository.existsById(id)) {
            throw   new NotFoundException("Location", id);
        }
    }

    private Location findEntityById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() ->  new NotFoundException("Location", id));
    }


}