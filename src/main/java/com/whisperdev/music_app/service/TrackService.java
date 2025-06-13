package com.whisperdev.music_app.service;

import com.whisperdev.music_app.model.Track;
import com.whisperdev.music_app.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackService {
    @Autowired private TrackRepository trackRepository;
    public List<Track> getAllByLimitAndCategory(int limit,String category) {
        Pageable pageable = PageRequest.of(0,limit, Sort.by("countPlay").descending());
        return trackRepository.findAllByCategory(category,pageable).getContent();
    }
}
