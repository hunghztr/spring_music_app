package com.whisperdev.music_app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "tracks")
@Data
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;
    @NotBlank(message = "Mô tả không được để trống")
    private String description;
    @NotBlank(message = "Thể loại không được để trống")
    private String category;
    @NotBlank(message = "Hình ảnh chưa được tải")
    private String imgUrl;
    @NotBlank(message = "Audio chưa được tải")
    private String trackUrl;
    private int countLike;
    private int countPlay;
    private int countDislike;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean isDeleted;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "track")
    private List<Comment> comments;

    @JsonIgnore
    @OneToMany(mappedBy = "track")
    private List<Like> likes;
    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
    }
    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }

}
