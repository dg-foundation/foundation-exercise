package com.foundation.mbta.service.repo;


import com.foundation.mbta.service.model.Stop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Stop Entity
 */
@Repository
public interface StopRepository extends JpaRepository<Stop, String> {

    /**
     * Query to find stops with 2 or more Routes running through it.
     *
     * @return
     */
    @Query(value="select ms.name, mr.long_name " +
            "from mbta_stops ms join mbta_routes mr on ms.route_id = mr.id \n" +
            "join (select count(name) c, name from mbta_stops ms group by ms.name having  c >= 2) " +
            "aa on ms.name=aa.name order by ms.name", nativeQuery = true)
    List<Object> stop2OrMoreRoutes();

    /**
     * Query to find number of Stops a city has
     *
     * @param city The name of the city
     * @return City and Stop information
     */
    @Query(value="select count(id) from mbta_stops where municipality=:city ", nativeQuery = true)
    List<Object> cityStops(String city) ;

    /**
     * Query to find List of Stops with the maximum number of escalators
     *
     * @return List of objects with Stop information
     */

    @Query(value="select stop_id, count(stop_id) from mbta_facilities group by stop_id, type \n" +
            "having  type = 'ESCALATOR' and count(stop_id)=\n" +
            "(select max(aa.c) from (\n" +
            "select count(stop_id) c, stop_id, type from mbta_facilities group by stop_id,type \n" +
            "having type = 'ESCALATOR') aa) ", nativeQuery = true)
    List<Object> mostEscalators();

}
