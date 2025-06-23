package com.whisperdev.music_app.controller;

import com.whisperdev.music_app.dto.StringResult;
import com.whisperdev.music_app.dto.page.PageResponse;
import com.whisperdev.music_app.dto.playlist.PlaylistRequest;
import com.whisperdev.music_app.dto.playlist.PlaylistResponse;
import com.whisperdev.music_app.model.Playlist;
import com.whisperdev.music_app.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/playlists")
public class PlaylistController {
    @Autowired private PlaylistService playlistService;

    @PostMapping("/empty")
    public ResponseEntity<?> emptyPlaylist(@RequestBody Playlist playlist) {
        playlistService.create(playlist);
        StringResult result = new StringResult();
        result.setResult("Tạo mới playlist");
        return ResponseEntity.ok(result);
    }
    @PutMapping
    public ResponseEntity<?> updatePlaylist(@RequestBody PlaylistRequest playlist) {
        PlaylistResponse response = playlistService.updatePlaylist(playlist);
        if(response == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("/by-user")
    public ResponseEntity<?> getByUser(@RequestParam(name = "current",defaultValue = "1") Integer current,
                                       @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize) {
        PageResponse<PlaylistResponse> response = playlistService.getPlaylistsByUser(current,pageSize);
        return ResponseEntity.ok(response);
    }
}
