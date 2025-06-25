package com.whisperdev.music_app.repository;

import com.whisperdev.music_app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    User findByUsername(String username);
    boolean existsByUsername(String username);
    User save(User user);
    User findByRefreshTokenAndUsername(String refreshToken, String username);
    Page<User> findAll(Pageable pageable);
    void deleteById(String id);
    Page<User> findByUsernameContainingIgnoreCase(String username,Pageable pageable);
}
