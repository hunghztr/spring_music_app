package com.whisperdev.music_app.utils.mapper;

import com.whisperdev.music_app.dto.track.TrackResponse;
import com.whisperdev.music_app.dto.user.UserResponse;
import com.whisperdev.music_app.model.Track;
import com.whisperdev.music_app.model.User;

public class TrackMapper {
    public static TrackResponse toTrackResponse(Track track) {
        TrackResponse trackResponse = new TrackResponse();
        if(track == null){
            return trackResponse;
        }
        trackResponse.setId(track.getId());
        trackResponse.setTitle(track.getTitle());
        trackResponse.setDescription(track.getDescription());
        trackResponse.setCategory(track.getCategory());
        trackResponse.setImgUrl(track.getImgUrl());
        trackResponse.setTrackUrl(track.getTrackUrl());
        trackResponse.setCountLike(track.getCountLike());
        trackResponse.setCountPlay(track.getCountPlay());
        trackResponse.setCountDislike(track.getCountDislike());
        UserResponse userResponse = UserMapper.toUserResponse(track.getUser());
        trackResponse.setUploader(userResponse);
        trackResponse.setCreatedAt(track.getCreatedAt());
        trackResponse.setUpdatedAt(track.getUpdatedAt());
        trackResponse.setDeleted(track.isDeleted());
        return trackResponse;
    }
}
