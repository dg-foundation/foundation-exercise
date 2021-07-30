package com.foundation.mbta.service.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Facility entity object
 */
@Entity
@Table(name = "mbta_facilities")
@Data
@NoArgsConstructor
public class Facility {

    @Id
    String id;
    String stop_id;
    String type;

    public Facility(String id, String stop_id, String type) {
        this.id = id;
        this.stop_id = stop_id;
        this.type = type;
    }
}
