package com.festi_ci.parti_ci.event;

import com.festi_ci.parti_ci.utils.enums.FeeType;

import java.time.LocalDateTime;

public record Response(Long id, String title, String description, String slug, LocalDateTime startAt, String startTime,
                       String locationName, EventCategoryModel eventCategory, Long organizerId, String templatePath, boolean isPrivate,
                       FeeType monetizationType, int maxCapacity, LocalDateTime updatedAt, LocalDateTime createdAt, String locationId,
                       String dressCode, boolean maxCapacityEnabled, String host, int guestCount) {
    public static Response fromEntity(EventModel event) {
        return new Response(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getSlug(),
                event.getStartAt(),
                event.getStartTime(),
                event.getLocationName(),
                event.getEventCategory(),
                event.getOrganizer() != null ? event.getOrganizer().getId() : null,
                event.getTemplatePath(),
                event.getIsPrivate(),
                event.getMonetizationType(),
                event.getMaxCapacity() != null ? event.getMaxCapacity() : 0,
                event.getUpdatedAt(),
                event.getCreatedAt(),
                event.getLocation() != null ? event.getLocation().getPlaceId() : null,
                event.getDressCode(),
                event.getMaxCapacityEnabled() != null ? event.getMaxCapacityEnabled() : false,
                event.getHost() != null ? event.getHost() : "Didier",
                event.getGuestList() != null ? event.getGuestList().size() : 0
        );
    }
}