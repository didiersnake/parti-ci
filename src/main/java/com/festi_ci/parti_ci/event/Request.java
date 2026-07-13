package com.festi_ci.parti_ci.event;

import com.festi_ci.parti_ci.enums.FeeType;
import com.festi_ci.parti_ci.enums.PrivacyStatus;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public record Request(@NotBlank String title, @NotBlank String templateCoverUUID, int maxCapacity,
                      PrivacyStatus privacyStatus, FeeType feeType, String dressCode,
                      @NotBlank LocalDateTime startAt, MultipartFile file, long categoryId) {
}