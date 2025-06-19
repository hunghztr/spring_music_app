package com.whisperdev.music_app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Slf4j
@Service
public class FileService {
    public Optional<Resource> getByName(String name,String folder){
        try {
            Path relativePath = Paths.get("uploads"+folder).toAbsolutePath().normalize();
            Path filePath = relativePath.resolve(name).normalize();
            if(!filePath.toFile().exists()){
                return Optional.empty();
            }
            return Optional.of(new UrlResource(filePath.toUri()));

        } catch (MalformedURLException e) {
            return Optional.empty();
        }
    }
    public String saveFile(MultipartFile file, String folder){
        try {
            Path relativePath = Paths.get("uploads" + folder).toAbsolutePath().normalize();
            Files.createDirectories(relativePath);
            double sizeInMB = file.getSize() / (1024.0 * 1024.0);
            String sizeFormatted = String.format("%.2f MB", sizeInMB);
            String timestamp = new java.text.SimpleDateFormat("ssmmHHddMMyyyy").format(new java.util.Date());
            String filename = StringUtils
                    .cleanPath(file.getOriginalFilename()+"-"+timestamp+"("+sizeFormatted+")");
            Path targetLocation = relativePath.resolve(filename);
            file.transferTo(targetLocation.toFile());
            return filename;
        }catch (Exception e){
            return e.getMessage();
        }
    }
    public String encodeFilenameRFC5987(String filename) {
        try {
            return URLEncoder.encode(filename, StandardCharsets.UTF_8.name()).replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            return "unknown.mp3";
        }
    }
}
