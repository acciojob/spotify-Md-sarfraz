package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spotify")
public class SpotifyController {
    @Autowired
    SpotifyService spotifyService;

    @PostMapping("/user")
    public User createUser(@RequestParam String name, @RequestParam String mobile) {
        return spotifyService.createUser(name, mobile);
    }

    @PostMapping("/artist")
    public Artist createArtist(@RequestParam String name) {
        return spotifyService.createArtist(name);
    }

    @PostMapping("/album")
    public Album createAlbum(@RequestParam String title, @RequestParam String artistName) {
        return spotifyService.createAlbum(title, artistName);
    }

    @PostMapping("/song")
    public Song createSong(@RequestParam String title, @RequestParam String albumName, @RequestParam int length) throws Exception {
        return spotifyService.createSong(title, albumName, length);
    }

    @PostMapping("/playlist/length")
    public Playlist createPlaylistOnLength(@RequestParam String mobile, @RequestParam String title, @RequestParam int length) throws Exception {
        return spotifyService.createPlaylistOnLength(mobile, title, length);
    }

    @PostMapping("/playlist/names")
    public Playlist createPlaylistOnName(@RequestParam String mobile, @RequestParam String title, @RequestBody List<String> songTitles) throws Exception {
        return spotifyService.createPlaylistOnName(mobile, title, songTitles);
    }

    @GetMapping("/playlist")
    public Playlist findPlaylist(@RequestParam String mobile, @RequestParam String playlistTitle) throws Exception {
        return spotifyService.findPlaylist(mobile, playlistTitle);
    }

    @PutMapping("/song/like")
    public Song likeSong(@RequestParam String mobile, @RequestParam String songTitle) throws Exception {
        return spotifyService.likeSong(mobile, songTitle);
    }

    @GetMapping("/artist/popular")
    public String mostPopularArtist() {
        return spotifyService.mostPopularArtist();
    }

    @GetMapping("/song/popular")
    public String mostPopularSong() {
        return spotifyService.mostPopularSong();
    }
}
