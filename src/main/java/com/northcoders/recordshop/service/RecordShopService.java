package com.northcoders.recordshop.service;

import com.northcoders.recordshop.model.Album;
import com.northcoders.recordshop.model.Genre;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface RecordShopService {
    List<Album> getAllAlbums();
    Album getAlbumById(long id);
    Album insertNewAlbum(Album album);
    ResponseEntity<Album> putAlbum(Album album, Long id);
    boolean deleteAlbum (long id);
    List<Album> getAllAlbumsByArtist(String artist);
    List<Album> getAllAlbumsByReleaseYear(Integer year);
    List<Album> getAllAlbumsByGenre (Genre genre);
    List<Album> getAllAlbumsByName (String albumName);
    List<Album> getAllAlbumsByMultipleParams (Map<String, String> params);
}
