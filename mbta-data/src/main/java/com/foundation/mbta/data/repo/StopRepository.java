package com.foundation.mbta.data.repo;

import com.foundation.mbta.data.model.Stop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Stop Entity
 */
@Repository
public interface StopRepository extends JpaRepository<Stop, String> {
}
