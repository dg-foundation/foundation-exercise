package com.foundation.mbta.service.repo;

import com.foundation.mbta.service.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Route Entity
 */
@Repository
public interface RouteRepository extends JpaRepository<Route, String> {

    /**
     * Query to get list of Routes with maximum Stops.
     *
     * @return List of objects with Route info
     */
     @Query(value="select  mr.long_name, count(mr.id)  from mbta_stops ms \n" +
            "join mbta_routes mr on mr.id= ms.route_id \n" +
            "group by route_id \n" +
            "having count(mr.id) = (select max(aa.c) from (\n" +
            "select count(mr.id) c  from mbta_stops ms \n" +
            "join mbta_routes mr on mr.id= ms.route_id \n" +
            "group by route_id ) aa) ", nativeQuery = true)
    List<Object> findRouteWithMaxStops();

    /**
     * Query to find list of Routes with minimum stops.
     *
     * @return List of objects with Route info
     */
    @Query(value="select  mr.long_name, count(mr.id)  from mbta_stops ms \n" +
            "join mbta_routes mr on mr.id= ms.route_id \n" +
            "group by route_id \n" +
            "having count(mr.id) = (select min(aa.c) from (\n" +
            "select count(mr.id) c  from mbta_stops ms \n" +
            "join mbta_routes mr on mr.id= ms.route_id \n" +
            "group by route_id ) aa) ", nativeQuery = true)
    List<Object> findRouteWithMinStops();
}
