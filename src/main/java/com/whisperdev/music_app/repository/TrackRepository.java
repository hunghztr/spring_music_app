package com.whisperdev.music_app.repository;

import com.whisperdev.music_app.model.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackRepository extends JpaRepository<Track, String>, JpaSpecificationExecutor<Track> {
    Page<Track> findAllByCategory(String category,Pageable pageable);
}
