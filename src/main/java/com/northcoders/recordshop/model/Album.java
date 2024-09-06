package com.northcoders.recordshop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "album")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(name = "album_name", nullable = false)
    String albumName;

    @Column(name = "artist", nullable = false)
    String artist;

    @Column(name = "release_year", nullable = false)
    Integer releaseYear;

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

    public Album(long id, String albumName, String artist, int releaseYear, Genre genre) {
        this.id = id;
        this.albumName = albumName;
        this.artist = artist;
        this.releaseYear = releaseYear;
        this.genre = genre;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getGenre() {
        return genre.toString();
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
}
