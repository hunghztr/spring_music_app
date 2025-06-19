package com.whisperdev.music_app.dto.track;

import com.whisperdev.music_app.dto.user.UserResponse;
import lombok.Data;

import java.time.Instant;

@Data
public class TrackResponse {
    private String id;
    private String title;
    private String description;
    private String category;
    private String imgUrl;
    private String trackUrl;
    private int countLike;
    private int countPlay;
    private int countDislike;
    private UserResponse uploader;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean isDeleted;

}
