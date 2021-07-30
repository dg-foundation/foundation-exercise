package com.foundation.mbta.data.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * Route entity object
 */
@Entity
@Table(name = "mbta_routes")
@Data
@NoArgsConstructor
public class Route {
    @Id
    String id;
    String long_name;
    String short_name;

    @ManyToMany
    List<Stop> stops;

    public Route(String id, String shortName, String longName) {
        this.id = id;
        this.short_name = shortName;
        this.long_name = longName;
    }

}
