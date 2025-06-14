package com.whisperdev.music_app.controller;

import com.whisperdev.music_app.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {
    @Autowired private FileService fileService;
    @GetMapping("/{name}")
    public ResponseEntity<?> getImgByName(@PathVariable("name") String name) {
        Optional<Resource> resourceOpt = this.fileService.getByName(name,"/img");
        if (resourceOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resourceOpt.get().getFilename() + "\"")
                .body(resourceOpt.get());
    }

}
