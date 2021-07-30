package com.foundation.mbta.service.repo;

import com.foundation.mbta.service.model.Line;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Line Entity
 */
@Repository
public interface LineRepository extends JpaRepository<Line, String> {
}
