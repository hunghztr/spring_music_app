package com.whisperdev.music_app.service;

import com.whisperdev.music_app.model.User;
import com.whisperdev.music_app.repository.UserRepository;
import com.whisperdev.music_app.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired private UserRepository userRepository;
    public User fetchUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }
    public User handleUpdateUser(User user, String id) {
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            User curUser = optional.get();
            curUser.setName(user.getName());
            curUser.setPassword(user.getPassword());
            curUser.setRole(user.getRole());
            return userRepository.save(curUser);
        }
        return null;
    }
    public ResponseCookie createCookie(String token, int maxAge) {
        return ResponseCookie.from("refresh_token", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(maxAge)
                .build();
    }
    public void updateUserToken(String token, String email) {
        User currentUser = this.fetchUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }
}
