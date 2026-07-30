package com.festi_ci.parti_ci.location;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data @Builder
public class LocationRequest {

    @NotBlank
    private String placeId;
    private String name;
     private String streetAddress;
     private String latitude;
     private String longitude;
     private String stateCode;
     private String countryCode;
    private String county;
    private String city;
    private String state;
}