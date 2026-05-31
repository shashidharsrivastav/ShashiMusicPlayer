package com.audio.webMusicPlayer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class SongEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String songName;

    @Lob
    @Column(name = "filename", columnDefinition = "BLOB")
    private byte[] filename;
    private Double fileSize;
}
