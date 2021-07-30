package com.foundation.mbta.service.repo;

import com.foundation.mbta.service.model.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Facility Entity
 */
@Repository
public interface FacilityRepository extends JpaRepository<Facility, String> {

}
