package com.whisperdev.music_app.controller;

import com.whisperdev.music_app.dto.comment.CommentResponse;
import com.whisperdev.music_app.dto.page.PageResponse;
import com.whisperdev.music_app.model.Comment;
import com.whisperdev.music_app.service.CommentService;
import com.whisperdev.music_app.service.TrackService;
import com.whisperdev.music_app.utils.exception.InvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {
    @Autowired private CommentService commentService;

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody Comment comment) throws InvalidException {
        CommentResponse commentResponse = this.commentService.save(comment);
        if(commentResponse == null){
            throw new InvalidException("Bài hát không tồn tại");
        }
        return ResponseEntity.ok(commentResponse);
    }
    @PostMapping("/get-by-track")
    public ResponseEntity<?> getByTrack(@RequestParam(name = "current",defaultValue = "1") Integer current,
                                        @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize,
                                        @RequestParam(name = "trackId") String trackId) throws InvalidException {
        PageResponse<CommentResponse> comments
                = this.commentService.getByTrackId(current,pageSize,trackId);
        if (comments == null){
            throw new InvalidException("không có comment");
        }
        return ResponseEntity.ok(comments);
    }
}
