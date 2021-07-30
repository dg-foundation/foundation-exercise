package com.foundation.mbta.data.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Line entity object
 */
@Entity
@Table(name = "mbta_lines")
@Data
@NoArgsConstructor
public class Line {
    @Id
    String id;
    String long_name;
    String short_name;

    public Line(String id, String shortName, String longName) {
        this.id = id;
        this.short_name = shortName;
        this.long_name = longName;
    }
}
