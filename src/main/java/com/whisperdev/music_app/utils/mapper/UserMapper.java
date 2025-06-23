package com.whisperdev.music_app.utils.mapper;

import com.whisperdev.music_app.dto.user.UserResponse;
import com.whisperdev.music_app.model.User;

public class UserMapper {
    public static UserResponse toUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        if(user == null) {
            return userResponse;
        }
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getUsername());
        userResponse.setUsername(user.getUsername());
        userResponse.setRole(user.getRole());
        userResponse.setType(user.getType());
        userResponse.setVerify(user.isVerify());
        userResponse.setAvatar(user.getAvatar());
        return userResponse;
    }
}
