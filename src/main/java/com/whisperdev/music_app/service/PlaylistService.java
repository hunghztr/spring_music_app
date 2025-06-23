package com.whisperdev.music_app.service;

import com.whisperdev.music_app.dto.page.MetaResponse;
import com.whisperdev.music_app.dto.page.PageResponse;
import com.whisperdev.music_app.dto.playlist.PlaylistRequest;
import com.whisperdev.music_app.dto.playlist.PlaylistResponse;
import com.whisperdev.music_app.dto.track.TrackResponse;
import com.whisperdev.music_app.model.Playlist;
import com.whisperdev.music_app.model.PlaylistDetails;
import com.whisperdev.music_app.model.Track;
import com.whisperdev.music_app.model.User;
import com.whisperdev.music_app.repository.PlaylistDetailsRepository;
import com.whisperdev.music_app.repository.PlaylistRepository;
import com.whisperdev.music_app.repository.TrackRepository;
import com.whisperdev.music_app.repository.UserRepository;
import com.whisperdev.music_app.utils.mapper.PlaylistMapper;
import com.whisperdev.music_app.utils.mapper.TrackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private PlaylistDetailsRepository playlistDetailsRepository;
    @Autowired
    private TrackRepository trackRepository;
    @Autowired
    private UserRepository userRepository;

    public Playlist create(Playlist playlist) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        playlist.setUser(user);
        return playlistRepository.save(playlist);
    }

    public PlaylistResponse updatePlaylist(PlaylistRequest playlistRequest) {
        PlaylistDetails details = new PlaylistDetails();
        List<String> unique = playlistRequest.getTrackIds().stream().distinct().toList();
        List<Track> tracks = new ArrayList<>();
        for (var u : unique) {
            var track = trackRepository.findById(u);
            if (track.isPresent()) {
                tracks.add(track.get());
            }
        }
        Optional<Playlist> playlist =
                playlistRepository.findById(playlistRequest.getId());

        if (playlist.isPresent()) {
            for (var track : tracks) {
                details.setPlaylist(playlist.get());
                details.setTrack(track);
                details = playlistDetailsRepository.save(details);
            }
        }

        PlaylistResponse playlistResponse = new PlaylistResponse();
        if (playlist.isPresent()) {
            playlistResponse.setId(playlist.get().getId());
            playlistResponse.setTitle(playlist.get().getTitle());
            playlistResponse.setPublic(playlist.get().isPublic());
            List<TrackResponse> trackResponses = tracks.stream()
                    .map(TrackMapper::toTrackResponse).toList();
            playlistResponse.setTracks(trackResponses);
            return playlistResponse;
        }
        return null;
    }

    public PageResponse<PlaylistResponse> getPlaylistsByUser(Integer current, Integer pageSize) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        Pageable pageable = PageRequest.of(current - 1, pageSize);

        Page<Playlist> playlistPage = playlistRepository.findByUserId(user.getId(), pageable);

        PageResponse<PlaylistResponse> response = new PageResponse<>();
        MetaResponse meta = new MetaResponse();

        meta.setCurrent(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(playlistPage.getTotalPages());
        meta.setTotal(playlistPage.getTotalElements());
        response.setMeta(meta);

        List<PlaylistResponse> playlistResponses = new ArrayList<>();

        playlistPage.getContent().forEach(i -> {
            List<PlaylistDetails> details = playlistDetailsRepository.findByPlaylistId(i.getId());
            List<Track> tracks = details.stream().map(PlaylistDetails::getTrack).distinct().toList();
            List<TrackResponse> trackResponses = tracks.stream().map(TrackMapper::toTrackResponse).toList();
            PlaylistResponse playlistResponse = new PlaylistResponse();
            playlistResponse.setId(i.getId());
            playlistResponse.setTitle(i.getTitle());
            playlistResponse.setPublic(i.isPublic());
            playlistResponse.setTracks(trackResponses);
            playlistResponses.add(playlistResponse);
        });

        response.setResult(playlistResponses);
        return response;
    }
}
