package com.whisperdev.music_app.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tracks")
@Data
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String title;
    private String description;
    private String category;
    private String imgUrl;
    private String trackUrl;
    private int countLike;
    private int countPlay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
