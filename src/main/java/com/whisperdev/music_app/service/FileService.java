package com.whisperdev.music_app.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import org.springframework.stereotype.Service;

import java.net.MalformedURLException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class FileService {
    public Optional<Resource> getByName(String name){
        try {
            Path relativePath = Paths.get("uploads").toAbsolutePath().normalize();
            Path filePath = relativePath.resolve(name).normalize();
            if(!filePath.toFile().exists()){
                return Optional.empty();
            }
            return Optional.of(new UrlResource(filePath.toUri()));

        } catch (MalformedURLException e) {
            return Optional.empty();
        }
    }
}
