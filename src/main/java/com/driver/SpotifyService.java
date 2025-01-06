package com.driver;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SpotifyService {
    SpotifyRepository spotifyRepository = new SpotifyRepository();

    public User createUser(String name, String mobile) {
        User user = new User(name, mobile);
        return spotifyRepository.saveUser(user);
    }

    public Artist createArtist(String name) {
        Artist artist = new Artist(name);
        return spotifyRepository.saveArtist(artist);
    }

    public Album createAlbum(String title, String artistName) {
        if (spotifyRepository.findArtistByName(artistName) == null) {
            createArtist(artistName);
        }
        Album album = new Album(title);
        return spotifyRepository.saveAlbum(album, artistName);
    }

    public Song createSong(String title, String albumName, int length) throws Exception {
        if (spotifyRepository.findAlbumByName(albumName) == null) {
            throw new Exception("Album does not exist");
        }
        Song song = new Song(title, length);
        return spotifyRepository.saveSong(song, albumName);
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        User user = spotifyRepository.findUserByMobile(mobile);
        if (user == null) {
            throw new Exception("User does not exist");
        }

        List<Song> songs = spotifyRepository.songs.stream()
                .filter(song -> song.getLength() == length)
                .collect(Collectors.toList());

        Playlist playlist = new Playlist(title);
        spotifyRepository.savePlaylist(playlist, user);
        spotifyRepository.playlistSongMap.put(playlist, songs);
        return playlist;
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        User user = spotifyRepository.findUserByMobile(mobile);
        if (user == null) {
            throw new Exception("User does not exist");
        }

        List<Song> songs = songTitles.stream()
                .map(spotifyRepository::findSongByTitle)
                .collect(Collectors.toList());

        Playlist playlist = new Playlist(title);
        spotifyRepository.savePlaylist(playlist, user);
        spotifyRepository.playlistSongMap.put(playlist, songs);
        return playlist;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        User user = spotifyRepository.findUserByMobile(mobile);
        if (user == null) {
            throw new Exception("User does not exist");
        }

        Playlist playlist = spotifyRepository.findPlaylistByTitle(playlistTitle);
        if (playlist == null) {
            throw new Exception("Playlist does not exist");
        }

        spotifyRepository.playlistListenerMap.get(playlist).add(user);
        spotifyRepository.userPlaylistMap.computeIfAbsent(user, k -> new ArrayList<>()).add(playlist);
        return playlist;
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        User user = spotifyRepository.findUserByMobile(mobile);
        if (user == null) {
            throw new Exception("User does not exist");
        }

        Song song = spotifyRepository.findSongByTitle(songTitle);
        if (song == null) {
            throw new Exception("Song does not exist");
        }

        spotifyRepository.songLikeMap.computeIfAbsent(song, k -> new ArrayList<>()).add(user);
        return song;
    }

    public String mostPopularArtist() {
        return spotifyRepository.artistAlbumMap.entrySet().stream()
                .max(Comparator.comparingInt(e -> e.getValue().stream()
                        .mapToInt(album -> spotifyRepository.albumSongMap.getOrDefault(album, new ArrayList<>())
                                .stream().mapToInt(song -> spotifyRepository.songLikeMap.getOrDefault(song, new ArrayList<>()).size())
                                .sum()).sum()))
                .map(e -> e.getKey().getName())
                .orElse(null);
    }

    public String mostPopularSong() {
        return spotifyRepository.songs.stream()
                .max(Comparator.comparingInt(song -> spotifyRepository.songLikeMap.getOrDefault(song, new ArrayList<>()).size()))
                .map(Song::getTitle)
                .orElse(null);
    }
}
