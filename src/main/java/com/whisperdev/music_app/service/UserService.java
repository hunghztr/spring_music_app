package com.whisperdev.music_app.service;

import com.whisperdev.music_app.dto.user.LoginResponse;
import com.whisperdev.music_app.model.User;
import com.whisperdev.music_app.repository.UserRepository;
import com.whisperdev.music_app.utils.SecurityUtil;
import com.whisperdev.music_app.utils.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired private UserRepository userRepository;
    @Autowired private SecurityUtil securityUtil;
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

    public void updateUserToken(String token, String email) {
        User currentUser = this.fetchUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }
    public boolean isExistByUsername(String username) {
        return this.userRepository.existsByUsername(username);
    }
    public User save(User user) {
        return this.userRepository.save(user);
    }
    public LoginResponse getLoginResponse(String username){
        LoginResponse response = new LoginResponse();
        User currentUserDB = this.fetchUserByUsername(username);
        if(currentUserDB != null) {
            response.setUser(UserMapper.toUserResponse(currentUserDB));
        }

        // create access token
        String accessToken = this.securityUtil.generateToken(currentUserDB,"access_token");
        response.setAccessToken(accessToken);

        // create refresh token
        String refreshToken = this.securityUtil.generateToken(currentUserDB,"refresh_token");
        response.setRefreshToken(refreshToken);
        // update user
        this.updateUserToken(refreshToken, currentUserDB.getUsername());
        return  response;
    }
}
