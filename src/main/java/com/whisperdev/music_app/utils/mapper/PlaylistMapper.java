package com.whisperdev.music_app.utils.mapper;

import com.whisperdev.music_app.dto.playlist.PlaylistResponse;
import com.whisperdev.music_app.dto.track.TrackResponse;
import com.whisperdev.music_app.model.Playlist;
import com.whisperdev.music_app.model.Track;

import java.util.List;
import java.util.stream.Collectors;

public class PlaylistMapper {
    public static PlaylistResponse toPlaylistResponse(Playlist playlist, List<Track> tracks) {
        PlaylistResponse response = new PlaylistResponse();
        if(playlist == null) return response;
        response.setId(playlist.getId());
        response.setTitle(playlist.getTitle());
        response.setPublic(playlist.isPublic());
        List<TrackResponse> responses = tracks.stream()
                .map(i -> TrackMapper.toTrackResponse(i)).collect(Collectors.toList());
        response.setTracks(responses);
        return response;
    }
}
