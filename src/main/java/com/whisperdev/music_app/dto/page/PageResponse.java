package com.whisperdev.music_app.dto.page;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse <T>{
    private MetaResponse meta;
    private List<T> result;
}
