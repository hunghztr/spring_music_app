package com.whisperdev.music_app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String username;
    private String password;
    private int age;
    private String gender;
    private String address;
    private String role;
    private String type;
    private boolean isVerify;
    @Column(columnDefinition = "TEXT")
    private String refreshToken;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean isDeleted;
    private String avatar;
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Track> tracks;
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Like> likes;
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    List<Playlist> playlists;
    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
    }
    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }

}
