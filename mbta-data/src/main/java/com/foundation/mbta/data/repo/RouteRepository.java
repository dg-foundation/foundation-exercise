package com.foundation.mbta.data.repo;

import com.foundation.mbta.data.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Route Entity
 */
@Repository
public interface RouteRepository extends JpaRepository<Route, String> {
}
