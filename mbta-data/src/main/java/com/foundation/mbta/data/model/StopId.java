package com.foundation.mbta.data.model;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Composite key for Stop entity object
 */
@EqualsAndHashCode
@NoArgsConstructor
public class StopId implements Serializable {

    String id;
    String route_id;

    public StopId( String id, String routeId) {
        this.id = id;
        this.route_id = routeId;
    }
}
