package org.ably.circular.city;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface CityRepository extends JpaRepository<City,Long> {
    boolean existsByName(String name);

    @Modifying
    @Query("UPDATE City c SET c.isActive = CASE WHEN c.isActive = true THEN false ELSE true END WHERE c.id = :id")
    void changeActivityStatus(Long id);

    Set<City> findAllByIsActiveTrue();

}
