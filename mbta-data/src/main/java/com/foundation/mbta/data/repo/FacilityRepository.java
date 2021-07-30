package com.foundation.mbta.data.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.foundation.mbta.data.model.Facility;

/**
 * Repository for Facility Entity
 */
@Repository
public interface FacilityRepository extends JpaRepository<Facility, String> {
}
