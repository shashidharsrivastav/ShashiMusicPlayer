package com.audio.webMusicPlayer.controller;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/music")
@CrossOrigin(origins = "*")
public class MusicController {
    private final String MUSIC_DIR ="C:/Users/shash/Music";

    @GetMapping(value = "/image/{songName}", produces = {MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> getAlbumArt(@PathVariable String songName){
        try{
            Path filePath = Paths.get(MUSIC_DIR).resolve(songName).normalize();
            File file = filePath.toFile();

            if(!file.exists()){
                return ResponseEntity.notFound().build();
            }

            Mp3File mp3File=new Mp3File(file.getAbsoluteFile());

            if(mp3File.hasId3v2Tag()){
                ID3v2 id3v2Tag = mp3File.getId3v2Tag();
                byte[] albumArtData = id3v2Tag.getAlbumImage();

                if(albumArtData!=null){
                    String mineType = id3v2Tag.getAlbumImageMimeType();
                    MediaType mediaType = MediaType.IMAGE_JPEG;
                    if(mineType !=null && mineType.contains("png")){
                        mediaType = mediaType.IMAGE_PNG;
                    }
                    return ResponseEntity.ok().contentType(mediaType).body(albumArtData);
                }
            }
        }catch (Exception e){
            System.out.println("Error extracting album art for: "+ songName + " -> " + e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }
}
