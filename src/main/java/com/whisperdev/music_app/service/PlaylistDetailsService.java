package com.whisperdev.music_app.service;

import com.whisperdev.music_app.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaylistDetailsService {
    @Autowired private PlaylistRepository playlistRepository;
}
