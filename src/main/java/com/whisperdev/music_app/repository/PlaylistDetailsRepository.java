package com.whisperdev.music_app.repository;

import com.whisperdev.music_app.model.PlaylistDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistDetailsRepository extends JpaRepository<PlaylistDetails, String> {
    List<PlaylistDetails> findByPlaylistId(String playlistId);
    List<PlaylistDetails> findByPlaylistIdIn(List<String> playlistIds);
}
