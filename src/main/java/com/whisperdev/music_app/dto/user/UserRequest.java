package com.whisperdev.music_app.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequest {
    private String id;
    private String name;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private String passwordConfirm;
    private int age;
    private String gender;
    private String address;
    private String avatar;
}
