package com.festi_ci.parti_ci.event;

import com.festi_ci.parti_ci.user.GuestModel;
import com.festi_ci.parti_ci.utils.enums.FeeType;
import com.festi_ci.parti_ci.utils.enums.PrivacyStatus;
import com.festi_ci.parti_ci.location.LocationModel;
import com.festi_ci.parti_ci.user.UserModel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity @Table(name = "events")
@NoArgsConstructor @Data @AllArgsConstructor @Builder
public class EventModel {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY) private UserModel organizer;
    @JoinColumn(name = "event_category_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY) private EventCategoryModel eventCategory;
    @Column(nullable = false) private String title;
    @Column(nullable = false) private String host;
    @Column(nullable = false) private String description;
    @Column( nullable = false) private String templatePath;
    // Event basic details
    private Boolean isPrivate = false;

    @Column(nullable = false) private LocalDateTime startAt;
    @Column(nullable = false) private String startTime;
    private LocalDateTime endsAt;
    private FeeType monetizationType = FeeType.Free;
    @Column(nullable = false) private String locationName;
    @JoinColumn(name = "place_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY) private LocationModel location;
    private String dressCode;
    private Boolean maxCapacityEnabled = false;
    private Boolean waitListEnabled = false;
    private List<GuestModel> waitList;
    private List<GuestModel> guestList;
    private Integer maxCapacity;
    // Slug link to the event page
    @Column(nullable = false, unique = true) private String slug;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now();}
    @PreUpdate protected void onUpdate() { updatedAt = LocalDateTime.now();}
}