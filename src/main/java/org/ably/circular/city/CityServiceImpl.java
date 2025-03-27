package org.ably.circular.city;

import lombok.RequiredArgsConstructor;
import org.ably.circular.exception.DuplicateEntityException;
import org.ably.circular.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    private void validateCityRequest(CityRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("City request cannot be null");
        }

        if (cityRepository.existsByName(request.getName().toLowerCase())) {
            throw new DuplicateEntityException("City", "name", request.getName());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void existsById(Long id) {
        if (!cityRepository.existsById(id)) {
            throw new NotFoundException("City", id);
        }
    }

    private City findEntityById(Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("City", id));
    }

    @Override
    public CityResponse save(City city) {
        City savedCity = cityRepository.save(city);
        return cityMapper.toResponse(savedCity);
    }

    @Override
    public CityResponse create(CityRequest request) {
        validateCityRequest(request);
        City city = cityMapper.toEntity(request);
        City savedCity = cityRepository.save(city);
        return cityMapper.toResponse(savedCity);
    }

    @Override
    public CityResponse update(Long id, CityRequest request) {
        validateCityRequest(request);
        City existingCity = findEntityById(id);
        cityMapper.updateEntityFromRequest(request, existingCity);
        City updatedCity = cityRepository.save(existingCity);
        return cityMapper.toResponse(updatedCity);
    }

    @Override
    public void delete(Long id) {
        existsById(id);
        cityRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public CityResponse findById(Long id) {
        City city = findEntityById(id);
        return cityMapper.toResponse(city);
    }

 @Override
 @Transactional(readOnly = true)
 public Set<CityResponse> findAll() {
     return cityRepository.findAll().stream()
             .map(cityMapper::toResponse)
             .collect(Collectors.toSet());
 }

    @Override
    public void changeActivityStatus(Long id) {
        existsById(id);
        cityRepository.changeActivityStatus(id);
    }

    @Override
    public Set<ActiveCityResponse> getAllActiveCities() {
        return cityRepository.findAllByIsActiveTrue().stream()
                .map(cityMapper::toActiveCityResponse)
                .collect(Collectors.toSet());
    }
}