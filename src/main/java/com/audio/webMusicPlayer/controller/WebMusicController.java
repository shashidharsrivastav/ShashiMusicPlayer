package com.audio.webMusicPlayer.controller;


import com.audio.webMusicPlayer.entity.SongEntity;
import com.audio.webMusicPlayer.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class WebMusicController {
    @Autowired
    private SongService service;

    @GetMapping(value = "/files")
    public List<String> getSongFile()
    {
        return service.listSongFileNames();
    }

    @GetMapping("/music/{filename}")
    public ResponseEntity<Resource> getSong(@PathVariable String filename) throws IOException {
        return service.getSongByFileName(filename);
    }

    @PostMapping(value = "/sendSong", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> songs(@RequestParam("file") MultipartFile file
            , @RequestParam("songName") String songName) throws IOException {
        SongEntity songEntity = service.uploadSongs(songName, file);
        return ResponseEntity.ok("File Upload successfully: " + songEntity.getSongName());
    }

    @GetMapping("/music/name/{songName}")
    public ResponseEntity<Resource> getSongByName(@PathVariable String songName){
        return service.getSongByName(songName);
    }

    @GetMapping("/player")
    public String getPlayerPage() {
        return "songs";
    }
}
