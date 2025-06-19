package com.whisperdev.music_app.dto;

import lombok.Data;


@Data

public class RestResponse<T> {
    private int status;
    private Object message;
    private T data;


}
