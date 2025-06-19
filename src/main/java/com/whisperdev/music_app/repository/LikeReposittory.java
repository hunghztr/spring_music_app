package com.whisperdev.music_app.repository;

import com.whisperdev.music_app.model.Like;
import com.whisperdev.music_app.model.Track;
import com.whisperdev.music_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeReposittory extends JpaRepository<Like, String> {
    List<Like> findByUser(User user);
    Like findByUserAndTrack(User user, Track track);
}
