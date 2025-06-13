package com.whisperdev.music_app.dto.track;

import com.whisperdev.music_app.dto.user.UserResponse;
import lombok.Data;

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
    private UserResponse uploader;
}
