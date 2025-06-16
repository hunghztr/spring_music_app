package com.whisperdev.music_app.dto.user;

import lombok.Data;

@Data
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private UserResponse user;

}
