package com.festi_ci.parti_ci.comments;

import com.festi_ci.parti_ci.event.EventModel;
import com.festi_ci.parti_ci.user.GuestModel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity @Table(name = "comments") @Builder @Data
@AllArgsConstructor @NoArgsConstructor
public class CommentModel {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false) private String content;
    @Column(nullable = false) private GuestModel author;
    @ManyToOne( fetch = FetchType.LAZY) @JoinColumn(name = "event_id", nullable = false)
    private EventModel event;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now();}
    @PreUpdate protected void onUpdate() { updatedAt = LocalDateTime.now();}
}