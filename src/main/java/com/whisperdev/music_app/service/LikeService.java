package com.whisperdev.music_app.service;

import com.whisperdev.music_app.dto.StringResult;
import com.whisperdev.music_app.dto.track.TrackResponse;
import com.whisperdev.music_app.model.Like;
import com.whisperdev.music_app.model.Track;
import com.whisperdev.music_app.model.User;
import com.whisperdev.music_app.repository.LikeReposittory;
import com.whisperdev.music_app.repository.TrackRepository;
import com.whisperdev.music_app.repository.UserRepository;
import com.whisperdev.music_app.utils.mapper.TrackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeService {
    @Autowired private LikeReposittory likeReposittory;
    @Autowired private TrackRepository trackRepository;
    @Autowired private UserRepository userRepository;
    public List<TrackResponse> getTrackByUserLike(Integer current, Integer pageSize) {
        Pageable pageable = PageRequest.of(current-1, pageSize);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        List<Like> likes = likeReposittory.findByUser(user);
        List<Track> tracks = likes.stream().map(i -> i.getTrack()).toList();
        List<TrackResponse> trackResponses = tracks.stream()
                .map(i -> TrackMapper.toTrackResponse(i)).toList();
        return trackResponses;
    }

    public StringResult interactTrack(String trackId, Integer isLike) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        StringResult stringResult = new StringResult();

        Track track = trackRepository.findById(trackId).get();
        if(isLike == 1) {
            Like like = new Like();
            like.setTrack(track);
            like.setUser(user);
            likeReposittory.save(like);
            track.setCountLike(track.getCountLike() + 1);
            trackRepository.save(track);
            stringResult.setResult("Success");
        }else{
            Like like = likeReposittory.findByUserAndTrack(user, track);
            likeReposittory.delete(like);
            track.setCountLike(track.getCountLike() - 1);
            trackRepository.save(track);
            stringResult.setResult("Success");
        }
        return stringResult;
    }

    public StringResult increasePlay(String trackId) {
        Track track = trackRepository.findById(trackId).get();
        track.setCountPlay(track.getCountPlay() + 1);
        trackRepository.save(track);
        StringResult stringResult = new StringResult();
        stringResult.setResult("Success");
        return stringResult;
    }
}
