package com.whisperdev.music_app.utils.mapper;

import com.whisperdev.music_app.dto.comment.CommentResponse;
import com.whisperdev.music_app.dto.track.TrackResponse;
import com.whisperdev.music_app.dto.user.UserResponse;
import com.whisperdev.music_app.model.Comment;

public class CommentMapper {
    public static CommentResponse toCommentResponse(Comment comment) {
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(comment.getId());
        commentResponse.setContent(comment.getContent());
        commentResponse.setMoment(comment.getMoment());
        commentResponse.setCreatedAt(comment.getCreatedAt());
        commentResponse.setUpdatedAt(comment.getUpdatedAt());
        commentResponse.setDeleted(comment.isDeleted());
        UserResponse user = UserMapper.toUserResponse(comment.getUser());
        TrackResponse track = TrackMapper.toTrackResponse(comment.getTrack());
        commentResponse.setUser(user);
        commentResponse.setTrack(track);
        return commentResponse;
    }
}
