package org.ably.circular.location;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location,Long> {

    Set<Location> findAllByIsActiveTrue();


   Set<Location> findAllByEnterprise_Id(Long id);
}
