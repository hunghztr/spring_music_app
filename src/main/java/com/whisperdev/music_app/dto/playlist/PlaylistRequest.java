package com.whisperdev.music_app.dto.playlist;

import lombok.Data;

import java.util.List;

@Data
public class PlaylistRequest {
    private String id;
    private String title;
    private boolean isPublic;
    private List<String> trackIds;
}
