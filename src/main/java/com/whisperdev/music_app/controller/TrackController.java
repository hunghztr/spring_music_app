package com.whisperdev.music_app.controller;

import com.whisperdev.music_app.dto.track.TrackRequest;
import com.whisperdev.music_app.dto.track.TrackResponse;
import com.whisperdev.music_app.model.Track;
import com.whisperdev.music_app.service.FileService;
import com.whisperdev.music_app.service.TrackService;
import com.whisperdev.music_app.utils.mapper.TrackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/tracks")
public class TrackController {
    @Autowired
    private FileService fileService;
    @Autowired
    private TrackService trackService;

    @GetMapping("/{name}")
    public ResponseEntity<?> getMp3ByName(@PathVariable("name") String name) {
        Optional<Resource> resourceOpt = this.fileService.getByName(name,"/mp3");
        if (resourceOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resourceOpt.get().getFilename() + "\"")
                .body(resourceOpt.get());
    }

    @PostMapping("/top")
    public ResponseEntity<?> getTopByCategory(@RequestBody TrackRequest request) {
        List<Track> tracks = this.trackService.getAllByLimitAndCategory(request.getLimit(),
                request.getCategory());
        List<TrackResponse> trackResponses = tracks.stream()
                .map(i -> TrackMapper.toTrackResponse(i)).toList();

        return ResponseEntity.ok()
                .body(trackResponses);
    }

}
