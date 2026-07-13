package com.festi_ci.parti_ci.fileManager;

import com.festi_ci.parti_ci.user.UserModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity @Table( name = "file_metadata")
@NoArgsConstructor @AllArgsConstructor @Builder @Data
public class FileModel {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false) private String fileName;
    @Column(nullable = false) private String storedName; // file path in MinIO
//    @Column(nullable = false) private String path;
    @Column(nullable = false) private String contentType;
    @Column(nullable = false) private long size;
    @Column(nullable = false) private LocalDateTime createdAt;
    @Column(nullable = false) private Long uploadedBy;

    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now();}
}