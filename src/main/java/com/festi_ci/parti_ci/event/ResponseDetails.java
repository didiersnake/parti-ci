package com.festi_ci.parti_ci.event;

import com.festi_ci.parti_ci.location.LocationModel;
import com.festi_ci.parti_ci.user.GuestModel;
import com.festi_ci.parti_ci.utils.enums.FeeType;

import java.time.LocalDateTime;
import java.util.List;

public record ResponseDetails(
        Long id, String title, String description, String slug, LocalDateTime startAt, String startTime,
        String locationName, EventCategoryModel eventCategory, Long organizerId, String templatePath, boolean isPrivate,
        FeeType monetizationType, int maxCapacity, LocalDateTime updatedAt, LocalDateTime createdAt, String locationId,
        String dressCode, boolean maxCapacityEnabled, String host, LocationModel location, List<GuestModel> guestList,
        Boolean waitListEnabled, List<GuestModel> waitList
) {
    public static ResponseDetails fromEntity(EventModel event) {
        return new ResponseDetails(
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
                event.getLocation() != null ? event.getLocation() : null,
                event.getGuestList() != null ? event.getGuestList() : null,
                event.getWaitListEnabled() != null ? event.getWaitListEnabled() : false,
                event.getWaitList() != null ? event.getWaitList() : null
        );
    }
}