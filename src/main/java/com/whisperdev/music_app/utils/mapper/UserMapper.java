package com.whisperdev.music_app.utils.mapper;

import com.whisperdev.music_app.dto.user.UserResponse;
import com.whisperdev.music_app.model.User;

public class UserMapper {
    public static UserResponse toUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getEmail());
        userResponse.setRole(user.getRole());
        userResponse.setType("SYSTEM");
        return userResponse;
    }
}
