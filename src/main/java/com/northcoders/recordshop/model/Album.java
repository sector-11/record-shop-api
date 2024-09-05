package com.northcoders.recordshop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "album")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    long id;

    @Column(name = "album_name", nullable = false)
    String albumName;

    @Column(name = "artist", nullable = false)
    String artist;

    @Column(name = "release_year", nullable = false)
    int releaseYear;

    @Column(name = "genre_id", nullable = false)
    Genre genre;

    public Album() {
    }

    public Album(String albumName, String artist, int releaseYear, Genre genre) {
        this.albumName = albumName;
        this.artist = artist;
        this.releaseYear = releaseYear;
        this.genre = genre;
    }
}
