package com.whisperdev.music_app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {
    @GetMapping
    public ResponseEntity<String> hello() {

        return ResponseEntity.ok("Hello World!");
    }
}
