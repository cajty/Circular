package org.ably.circular.city;

import java.util.Set;

public interface CityService {
    void existsById(Long id);

    CityResponse save(City city);

    CityResponse create(CityRequest cityRequest);

    CityResponse update(Long id, CityRequest cityRequest);

    void delete(Long id);

    CityResponse findById(Long id);

   Set<CityResponse> findAll();

    void changeActivityStatus(Long id);
    Set<ActiveCityResponse> getAllActiveCities();

}