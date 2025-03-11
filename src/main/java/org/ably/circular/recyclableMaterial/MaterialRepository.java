package org.ably.circular.recyclableMaterial;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> , JpaSpecificationExecutor<Material> {

   @Modifying
   @Query("UPDATE Material m SET m.status = :newStatus WHERE m.availableUntil < :now")
   int markMaterialsAsExpired(
   @Param("newStatus") MaterialStatus newStatus,
   @Param("now") Date now
   );
}
