package com.whisperdev.music_app.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Instant likedAt;
    private boolean isDeleted;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    private Track track;
    @PrePersist
    public void prePersist() {
        this.likedAt = Instant.now();
    }
}
