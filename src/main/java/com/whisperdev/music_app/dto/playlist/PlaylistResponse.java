package com.whisperdev.music_app.dto.playlist;

import com.whisperdev.music_app.dto.track.TrackResponse;
import lombok.Data;

import java.util.List;

@Data
public class PlaylistResponse {
    private String id;
    private String title;
    private boolean isPublic;
    private List<TrackResponse> tracks;
}
