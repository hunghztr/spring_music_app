package com.whisperdev.music_app.controller;

import com.whisperdev.music_app.dto.StringResult;
import com.whisperdev.music_app.dto.track.TrackResponse;
import com.whisperdev.music_app.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/likes")
public class LikeController {

    @Autowired private LikeService likeService;
    @PostMapping("/increase-view")
    public ResponseEntity<StringResult> icreaseView(@RequestParam(name = "trackId") String trackId) {
        StringResult result = likeService.increasePlay(trackId);
        return ResponseEntity.ok(result);
    }
    @GetMapping
    public ResponseEntity<?> getTrackByUserLikes(
            @RequestParam(name = "current",defaultValue = "1") Integer current,
            @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize
    ){
        List<TrackResponse> responses = this.likeService.getTrackByUserLike(current,pageSize);
        return ResponseEntity.ok(responses);
    }
    @PostMapping
    public ResponseEntity<?> interactTrack(@RequestParam(name = "trackId",defaultValue = "") String trackId,
                                           @RequestParam(name = "isLike",defaultValue = "1") Integer isLike){
        StringResult result = this.likeService.interactTrack(trackId,isLike);
        return ResponseEntity.ok(result);
    }
}
