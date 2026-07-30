package com.festi_ci.parti_ci.albums;

import com.festi_ci.parti_ci.event.EventModel;
import com.festi_ci.parti_ci.fileManager.FileModel;
import com.festi_ci.parti_ci.user.GuestModel;
import com.festi_ci.parti_ci.user.UserModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

@Entity @Table(name = "shared_albums") @Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class SharedAlbumModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "event_id", nullable = false)
    private EventModel event;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "file_id", nullable = false)
    private List<FileModel> filePath;
    @JdbcTypeCode(SqlTypes.JSON)
    private GuestModel author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now();}
    @PreUpdate protected void onUpdate() { updatedAt = LocalDateTime.now();}
}