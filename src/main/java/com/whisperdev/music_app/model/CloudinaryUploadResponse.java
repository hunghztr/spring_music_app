package com.whisperdev.music_app.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CloudinaryUploadResponse {
    @JsonProperty("asset_id")
    private String assetId;

    @JsonProperty("public_id")
    private String publicId;

    private long version;

    @JsonProperty("version_id")
    private String versionId;

    private String signature;
    private int width;
    private int height;
    private String format;

    @JsonProperty("resource_type")
    private String resourceType;

    @JsonProperty("created_at")
    private String createdAt;

    private List<String> tags;
    private int pages;
    private long bytes;
    private String type;
    private String etag;
    private boolean placeholder;
    private String url;

    @JsonProperty("secure_url")
    private String secureUrl;

    @JsonProperty("playback_url")
    private String playbackUrl;

    @JsonProperty("asset_folder")
    private String assetFolder;

    @JsonProperty("display_name")
    private String displayName;

    private boolean existing;

    private Audio audio;
    private Video video;

    @JsonProperty("is_audio")
    private boolean isAudio;

    @JsonProperty("bit_rate")
    private int bitRate;

    private double duration;

    @JsonProperty("original_filename")
    private String originalFilename;

    @Data
    public static class Audio {
        private String codec;

        @JsonProperty("bit_rate")
        private String bitRate;

        private int frequency;
        private int channels;

        @JsonProperty("channel_layout")
        private String channelLayout;
    }

    @Data
    public static class Video {
        // Trường "video" là object rỗng trong JSON mẫu, bạn có thể bổ sung nếu Cloudinary trả về thông tin chi tiết sau này.
    }
}
