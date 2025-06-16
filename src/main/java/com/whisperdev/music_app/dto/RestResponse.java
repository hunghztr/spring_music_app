package com.whisperdev.music_app.dto;

import lombok.Data;


@Data

public class RestResponse<T> {
    private int status;
    private String message;
    private T data;


}
