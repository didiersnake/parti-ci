package com.festi_ci.parti_ci.event;

import com.festi_ci.parti_ci.location.LocationRequest;
import com.festi_ci.parti_ci.utils.enums.FeeType;
import com.festi_ci.parti_ci.utils.enums.PrivacyStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record Request(@NotBlank String title, String templateCoverUUID, int maxCapacity, String description,
                      FeeType feeType, String dressCode, boolean isPrivate, String startTime,
                      @NotNull LocationRequest location, boolean maxCapacityEnabled, boolean waitListEnabled,
                      @NotBlank String date, MultipartFile file, String categoryCode, String locationName, String host ) {
}