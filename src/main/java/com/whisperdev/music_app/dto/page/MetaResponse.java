package com.whisperdev.music_app.dto.page;

import lombok.Data;

@Data
public class MetaResponse {
    long current;
    long pageSize;
    long pages;
    long total;
}
