package org.ably.circular.location;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;


public interface LocationRepository extends JpaRepository<Location,Long> {

    Set<Location> findAllByEnterprise_IdAndIsActiveTrue(Long id);


   Page<Location> findAllByEnterprise_Id(Long enterpriseId, Pageable pageable);

      @Modifying
    @Query("UPDATE Location l SET l.isActive = CASE WHEN l.isActive = true THEN false ELSE true END WHERE l.id = :id")
    void changeLocationStatus(Long id);
}
