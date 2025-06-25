package com.whisperdev.music_app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

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
    @JsonIgnore
    @ManyToMany(mappedBy = "permissions")
    private List<Role> roles;
    public Permission() {
    }

    public Permission(String name, String method, String path) {
        this.name = name;
        this.method = method;
        this.path = path;
    }
}
