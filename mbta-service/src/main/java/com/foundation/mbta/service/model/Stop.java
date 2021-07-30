package com.foundation.mbta.service.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Stop entity object
 */
@Entity
@IdClass(StopId.class)
@Table(name = "mbta_stops")
@Data
@NoArgsConstructor
public class Stop {
    @Id
    private String id;
    @Id
    private String route_id;

    private String name;
    private String municipality;

    @OneToMany
    List<Facility> facilities;

    public Stop(String id, String route_id, String name, String municipality) {
        this.id = id;
        this.route_id = route_id;
        this.name = name;
        this.municipality = municipality;
    }

}
