package com.whisperdev.music_app.repository;

import com.whisperdev.music_app.model.Track;
import com.whisperdev.music_app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface TrackRepository extends JpaRepository<Track, String>, JpaSpecificationExecutor<Track> {
    Page<Track> findAllByCategoryAndCreatedAtAfter(String category, Instant fromDate, Pageable pageable);
    void deleteById(String id);
    Page<Track> findByUser(User user, Pageable pageable);
    Page<Track> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String title, String category, Pageable pageable);
}
