package org.ably.circular.MaterialCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    boolean existsByName(String name);

    @Modifying
   @Query("UPDATE Category c SET c.isActive = CASE WHEN c.isActive = true THEN false ELSE true END WHERE c.id = :id")
    void changeActivityStatus(Long id);
}
