package com.festi_ci.parti_ci.location;

import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LocationService {
    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public LocationModel createLocation(LocationRequest location) {
        LocationModel existingLocation = locationRepository.findByPlaceId(location.getPlaceId())
                .orElse(null);
        if (existingLocation != null) {
            return existingLocation;
        }
        GeometryFactory geometryFactory = new GeometryFactory();
        double longitude = Double.parseDouble(location.getLongitude());
        double latitude = Double.parseDouble(location.getLatitude());
        Point point = geometryFactory.createPoint(new Coordinate(latitude, longitude));
        LocationModel locationData = LocationModel.builder()
                .placeId(location.getPlaceId())
                .city(location.getCity())
                .state(location.getState())
                .stateCode(location.getStateCode())
                .countryCode(location.getCountryCode())
                .county(location.getCounty())
                .name(location.getName())
                .streetAddress(location.getStreetAddress())
                .point(point)
                .build();
        return locationRepository.save(locationData);
    }

    public LocationModel getLocation(String placeId) {
        return locationRepository.findByPlaceId(placeId).orElseThrow(
                () -> new RuntimeException("Location not found")
        );
    }
}