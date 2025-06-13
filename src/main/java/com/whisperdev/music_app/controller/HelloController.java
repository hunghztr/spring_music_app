package com.whisperdev.music_app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class HelloController {
    @GetMapping
    public String hello() {
        String relativePath = "uploads";
        Path uploadPath = Paths.get(relativePath).toAbsolutePath().normalize();
        System.out.println("Absolute path: " + uploadPath);
        return "Hello World "+uploadPath;
    }
}
