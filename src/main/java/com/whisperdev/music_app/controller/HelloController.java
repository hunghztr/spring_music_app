package com.whisperdev.music_app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whisperdev.music_app.model.CloudinaryUploadResponse;
import com.whisperdev.music_app.service.CloudinaryService;
import com.whisperdev.music_app.utils.exception.InvalidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RestController
public class HelloController {
    @Autowired private CloudinaryService cloudinaryService;
    @Autowired private ObjectMapper objectMapper;
    @GetMapping
    public ResponseEntity<String> hello() {

        return ResponseEntity.ok("Hello World!");
    }
    @GetMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("fileUpload") MultipartFile file) throws InvalidException {
        CloudinaryUploadResponse result = null;
        try {
            result = cloudinaryService.uploadToCloudinary(file,"mp3");
        } catch (Exception e) {
            throw new InvalidException(e.getMessage());
        }
        return ResponseEntity.ok(result.getSecureUrl());
    }
}
