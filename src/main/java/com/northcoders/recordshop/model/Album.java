package com.northcoders.recordshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;

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

    @JsonIgnore
    public Genre getGenreAsGenre() {
        return this.genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return Objects.equals(id, album.id) && Objects.equals(albumName, album.albumName) && Objects.equals(artist, album.artist) && Objects.equals(releaseYear, album.releaseYear) && genre == album.genre;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, albumName, artist, releaseYear, genre);
    }

    public boolean anyFieldOtherThanIdAIsNull() {
        return this.albumName == null || this.artist == null || this.genre == null || this.releaseYear == null;
    }
}
