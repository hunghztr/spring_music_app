package com.whisperdev.music_app.controller;

import com.whisperdev.music_app.dto.page.PageResponse;
import com.whisperdev.music_app.dto.track.TrackRequest;
import com.whisperdev.music_app.dto.track.TrackResponse;
import com.whisperdev.music_app.model.Track;
import com.whisperdev.music_app.model.User;
import com.whisperdev.music_app.service.FileService;
import com.whisperdev.music_app.service.TrackService;
import com.whisperdev.music_app.utils.exception.InvalidException;
import com.whisperdev.music_app.utils.mapper.TrackMapper;
import jakarta.validation.Valid;
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

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(name = "search",defaultValue = "undefined") String search,
            @RequestParam(name = "current",defaultValue = "1") Integer current,
            @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize){
        PageResponse<TrackResponse> response = this.trackService.getBySearch(search,current,pageSize);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/get-id/{id}")
    public ResponseEntity<TrackResponse> getTrack(@PathVariable("id") String id) {
        Track track = trackService.getById(id);
        TrackResponse trackResponse = TrackMapper.toTrackResponse(track);
        return ResponseEntity.ok(trackResponse);
    }
    @PostMapping("/user")
    public ResponseEntity<?> getTracksByUser(@RequestParam(name = "current",defaultValue = "1") Integer current,
                                             @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize,
                                             @RequestBody User user){
        PageResponse<TrackResponse> pageResponse = trackService.getTracksByUser(user.getId(),current,pageSize);
        return ResponseEntity.ok(pageResponse);
    }

    @PostMapping
    public ResponseEntity<?> createTrack(@Valid @RequestBody Track entity) {
        Track track = trackService.save(entity);
        return ResponseEntity.ok(track);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTrack(@PathVariable("id") String id) {
        trackService.deleteById(id);
        return ResponseEntity.ok().body(null);
    }
    @GetMapping
    public ResponseEntity<?> getAllTracks(@RequestParam(name = "current",defaultValue = "1") Integer current
    ,@RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize) throws InvalidException {
        List<Track> tracks = trackService.getAllByPage(current,pageSize);
        List<TrackResponse> trackResponses = tracks.stream()
                .map(i -> TrackMapper.toTrackResponse(i)).toList();
        if(trackResponses.size() == 0){
            throw new InvalidException("Không có bài hát nào");
        }
        return ResponseEntity.ok(trackResponses);
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> getMp3ByName(@PathVariable("name") String name) {
        Optional<Resource> resourceOpt = this.fileService.getByName(name,"/mp3");
        if (resourceOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // mã hoá tên file tiếng viêệt có dấu
        String encodedFilename = fileService.encodeFilenameRFC5987(resourceOpt.get().getFilename());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + encodedFilename+ "\"")
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
