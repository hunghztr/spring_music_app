package com.whisperdev.music_app.dto.user;

import lombok.Data;

@Data
public class UserResponse {
    private String id;
    private String email;
    private String name;
    private String role;
    private String type;
}
