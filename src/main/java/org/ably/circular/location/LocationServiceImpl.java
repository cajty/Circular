package org.ably.circular.location;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.ably.circular.enterprise.Enterprise;
import org.ably.circular.exception.BusinessException;
import org.ably.circular.security.CurrentUserProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ably.circular.exception.NotFoundException;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;
    private final CurrentUserProvider currentUserProvider;


    private void validateLocationRequest(LocationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Location request cannot be null");
        }
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
         Enterprise enterprise = currentUserProvider.getCurrentUserEnterpriseOrThrow();
         location.setEnterprise(enterprise);

        Location savedLocation = locationRepository.save(location);
        return locationMapper.toResponse(savedLocation);
    }

    @Override
    @Transactional
    public LocationResponse update(Long id, LocationRequest request) {
        Location existingLocation = findEntityById(id);
        validateLocationRequest(request);
        locationMapper.updateEntityFromRequest(request, existingLocation);
        return locationMapper.toResponse(existingLocation);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Location location = findEntityById(id);
        if(location.getMaterials().isEmpty()){
            locationRepository.delete(location);
        }else{
            throw new BusinessException("Location has associated materials and cannot be deleted");
        }
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

    @Override
    @Transactional(readOnly = true)
    public Page<LocationResponse> getAllLocationOfEnterprise(Pageable pageable) {
        Long enterpriseId = currentUserProvider.getCurrentUserEnterpriseOrThrow().getId();
        return locationRepository.findAllByEnterprise_Id(enterpriseId, pageable)
                .map(locationMapper::toResponse);
    }

     @Override
    @Transactional(readOnly = true)
    public Set<ActiveLocationResponse> getAllActiveLocationsOfEnterprise() {
         Long enterpriseId = currentUserProvider.getCurrentUserEnterpriseOrThrow().getId();
        return locationRepository.findAllByEnterprise_IdAndIsActiveTrue(enterpriseId).stream()
                .map(locationMapper::toActiveLocationResponse)
                .collect(Collectors.toSet());
    }





    @Override
    public void changeActivityStatus(Long id) {
        existsById(id);
        locationRepository.changeLocationStatus(id);
    }

    private Location findEntityById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() ->  new NotFoundException("Location", id));
    }


}