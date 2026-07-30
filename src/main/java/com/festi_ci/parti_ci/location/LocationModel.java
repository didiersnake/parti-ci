package com.festi_ci.parti_ci.location;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;


@Entity @Data
@AllArgsConstructor @NoArgsConstructor @Builder
@Table(name = "locations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"place_id"}),
//        create indexes
        indexes = @Index(name = "place_id_idx", columnList = "place_id")
)
public class LocationModel {
    @Id
    @Column(name = "place_id", nullable = false) private String placeId;
    @Column(name = "name", nullable = false) private String name;
    @Column(name = "address", nullable = false) private String streetAddress;
    @Column(nullable = false) private Point point;
    @Column(nullable = false) private String stateCode;
    @Column(nullable = false) private String countryCode;
    @Column(nullable = false) private String county;
    @Column(nullable = false) private String city;
    @Column(nullable = false) private String state;
}