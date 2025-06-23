package com.whisperdev.music_app.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "permissions")
@Data
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String method;
    private String path;

    public Permission() {
    }

    public Permission(String name, String method, String path) {
        this.name = name;
        this.method = method;
        this.path = path;
    }
}
