package com.whisperdev.music_app.controller;

import com.whisperdev.music_app.dto.StringResult;
import com.whisperdev.music_app.service.CloudinaryService;
import com.whisperdev.music_app.service.FileService;
import com.whisperdev.music_app.utils.exception.InvalidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class FileController {
    @Autowired private FileService fileService;
    @GetMapping("/images/avatar/{name}")
    public ResponseEntity<Resource> getAvatar(@PathVariable("name") String name) {
        Optional<Resource> resourceOpt = this.fileService.getByName(name,"/avatar");
        if (resourceOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        String encodeFile = fileService.encodeFilenameRFC5987(resourceOpt.get().getFilename());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + encodeFile + "\"")
                .body(resourceOpt.get());
    }
    @GetMapping("/images/{name}")
    public ResponseEntity<?> getImgByName(@PathVariable("name") String name) {
        Optional<Resource> resourceOpt = this.fileService.getByName(name,"/img");
        if (resourceOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        String encodeFile = fileService.encodeFilenameRFC5987(resourceOpt.get().getFilename());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + encodeFile + "\"")
                .body(resourceOpt.get());
    }
    @PostMapping("files/upload-mp3")
    public ResponseEntity<?> uploadFile(@RequestParam("fileUpload") MultipartFile file) throws InvalidException {
        String result = fileService.saveFile(file,"/mp3");
        StringResult stringResult = new StringResult();
        int index = result.indexOf("/video");
        String sub = result.substring(index);
        stringResult.setResult(sub);
        return ResponseEntity.ok().body(stringResult);
    }
    @PostMapping("files/upload-img")
    public ResponseEntity<?> uploadFileImg(@RequestParam("fileUpload") MultipartFile file) throws InvalidException {
        String result = fileService.saveFile(file,"/img");
        StringResult stringResult = new StringResult();
        int index = result.indexOf("/image");
        String sub = result.substring(index);
        stringResult.setResult(sub);
        return ResponseEntity.ok().body(stringResult);
    }
    @PostMapping("files/upload-avatar")
    public ResponseEntity<?> uploadFileAvatar(@RequestParam("fileUpload") MultipartFile file) throws InvalidException {
        String result = fileService.saveFile(file,"/avatar");
        int index = result.indexOf("/image");
        String sub = result.substring(index);
        StringResult stringResult = new StringResult();
        stringResult.setResult(sub);
        return ResponseEntity.ok().body(stringResult);
    }
}
