package com.whisperdev.music_app.dto.comment;

import com.whisperdev.music_app.dto.track.TrackResponse;
import com.whisperdev.music_app.dto.user.UserResponse;
import com.whisperdev.music_app.model.Comment;
import lombok.Data;

import java.time.Instant;

@Data
public class CommentResponse {
    private String id;
    private String content;
    private int moment;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean isDeleted;
    private UserResponse user;
    private TrackResponse track;
}
