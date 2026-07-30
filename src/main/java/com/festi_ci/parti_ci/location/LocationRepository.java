package com.festi_ci.parti_ci.location;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<LocationModel, String> {

    Optional<LocationModel> findByPlaceId(String placeId);
}