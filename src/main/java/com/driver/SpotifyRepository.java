package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository() {
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User saveUser(User user) {
        users.add(user);
        return user;
    }

    public Artist saveArtist(Artist artist) {
        artists.add(artist);
        artistAlbumMap.put(artist, new ArrayList<>());
        return artist;
    }

    public Album saveAlbum(Album album, String artistName) {
        Artist artist = findArtistByName(artistName);
        if (artist != null) {
            albums.add(album);
            artistAlbumMap.get(artist).add(album);
        }
        return album;
    }

    public Song saveSong(Song song, String albumName) {
        Album album = findAlbumByName(albumName);
        if (album != null) {
            songs.add(song);
            albumSongMap.computeIfAbsent(album, k -> new ArrayList<>()).add(song);
        }
        return song;
    }

    public Playlist savePlaylist(Playlist playlist, User creator) {
        playlists.add(playlist);
        creatorPlaylistMap.put(creator, playlist);
        playlistListenerMap.put(playlist, new ArrayList<>(Collections.singleton(creator)));
        playlistSongMap.put(playlist, new ArrayList<>());
        return playlist;
    }

    public Artist findArtistByName(String name) {
        return artists.stream().filter(a -> a.getName().equals(name)).findFirst().orElse(null);
    }

    public Album findAlbumByName(String name) {
        return albums.stream().filter(a -> a.getTitle().equals(name)).findFirst().orElse(null);
    }

    public Song findSongByTitle(String title) {
        return songs.stream().filter(s -> s.getTitle().equals(title)).findFirst().orElse(null);
    }

    public User findUserByMobile(String mobile) {
        return users.stream().filter(u -> u.getMobile().equals(mobile)).findFirst().orElse(null);
    }

    public Playlist findPlaylistByTitle(String title) {
        return playlists.stream().filter(p -> p.getTitle().equals(title)).findFirst().orElse(null);
    }
}
