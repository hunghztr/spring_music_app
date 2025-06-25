package com.whisperdev.music_app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whisperdev.music_app.model.CloudinaryUploadResponse;
import com.whisperdev.music_app.model.MultipartInputStreamFileResource;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CloudinaryService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired private ObjectMapper objectMapper;
    public CloudinaryUploadResponse uploadToCloudinary(MultipartFile file, String folder) throws Exception {
        String uploadUrl = "https://api.cloudinary.com/v1_1/dk5e2i4r9/image/upload";
        if(folder.equals("/mp3")){
            uploadUrl = "https://api.cloudinary.com/v1_1/dk5e2i4r9/video/upload";
        }
        String uploadPreset = "cloudinary-project";

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new MultipartInputStreamFileResource(file));
        body.add("upload_preset", uploadPreset);
        body.add("folder",folder);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(uploadUrl, requestEntity, String.class);
        return objectMapper.readValue(response.getBody(), CloudinaryUploadResponse.class);
    }
}
