package com.audio.webMusicPlayer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebPageController {
    @GetMapping("/player")
    public String getPlayerPage() {
        // Yeh templates/songs.html ko hi return karega
        return "songs";
    }
}
