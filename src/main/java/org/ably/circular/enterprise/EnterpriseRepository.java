package org.ably.circular.enterprise;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnterpriseRepository extends JpaRepository<Enterprise,Long> {

   @Query("SELECT DISTINCT e FROM Enterprise e " +
       "JOIN e.locations l " +
       "JOIN l.city c " +
       "WHERE c.name = :cityName")
List<Enterprise> findByCityName(@Param("cityName") String cityName);

boolean existsByRegistrationNumber(String registrationNumber);
}


