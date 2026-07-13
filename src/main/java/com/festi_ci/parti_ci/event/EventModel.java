package com.festi_ci.parti_ci.event;

import com.festi_ci.parti_ci.enums.FeeType;
import com.festi_ci.parti_ci.enums.PrivacyStatus;
import com.festi_ci.parti_ci.user.UserModel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity @Table(name = "events")
@NoArgsConstructor @Data @AllArgsConstructor @Builder
public class EventModel {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY) private UserModel organizerId;
    @JoinColumn(name = "event_category_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY) private EventCategoryModel eventCategoryId;
    @Column(nullable = false) private String title;
    @Column( nullable = false) private String templateCoverUUID;
    // Event basic details
    @Column(name = "privacy_status", nullable = false)
    private PrivacyStatus privacyStatus = PrivacyStatus.Public;
    @Column(nullable = false) private LocalDateTime startAt;
    private FeeType feeType = FeeType.Free;
    private String dressCode;
    private int maxCapacity;
    // Slug link to the event page
    @Column(nullable = false, unique = true) private String slug;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now();}
    @PreUpdate protected void onUpdate() { updatedAt = LocalDateTime.now();}
}