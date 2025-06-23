package com.whisperdev.music_app.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "playlist_details")
public class PlaylistDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id")
    private Track track;
}
