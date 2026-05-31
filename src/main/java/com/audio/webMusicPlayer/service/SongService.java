package com.audio.webMusicPlayer.service;

import com.audio.webMusicPlayer.entity.SongEntity;
import com.audio.webMusicPlayer.repository.SongsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class SongService {

    @Autowired
    private SongsRepository repository;

    private final String FOLDER_PATH = "C:/Users/shash/Music";

    public SongEntity uploadSongs(String songName, MultipartFile file) throws IOException {
        File folder = new File(FOLDER_PATH);

        if (!folder.exists()) folder.mkdir();

        String originalFileName = file.getOriginalFilename();
        String filePath = FOLDER_PATH + File.separator + originalFileName;
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            Files.copy(file.getInputStream(), path);
        } else {
            System.out.println("File already exists");
        }

        SongEntity entity = new SongEntity();
        entity.setFileSize((double) file.getSize() / (1024 * 1024));

        entity.setSongName(originalFileName);

        return repository.save(entity);
    }

    public List<SongEntity> findAllSongs() {
        return repository.findAll();
    }

    public ResponseEntity<Resource> getSongByFileName(String fileName) throws FileNotFoundException {
        File file = new File(FOLDER_PATH + File.separator + fileName);

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"")
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .body(resource);
    }

    public ResponseEntity<Resource> getSongByName(String songName) {
        try {
            File file = new File(FOLDER_PATH + File.separator + songName);

            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            Resource resource = new FileSystemResource(file);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"")
                    .contentType(MediaType.parseMediaType("audio/mpeg"))
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public List<String> listSongFileNames() {
        File folder = new File(FOLDER_PATH);
        if (!folder.exists() || !folder.isDirectory()) {
            return java.util.Collections.emptyList();
        }

        File[] files = folder.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".mp3") || name.toLowerCase().endsWith(".wav")
        );

        if (files == null) {
            return java.util.Collections.emptyList();
        }

        return java.util.Arrays.stream(files)
                .map(File::getName)
                .collect(java.util.stream.Collectors.toList());
    }
}
