package com.northcoders.recordshop.service;

import com.northcoders.recordshop.model.Album;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RecordShopService {
    List<Album> getAllAlbums();
    Album getAlbumById(long id);
    Album insertNewAlbum(Album album);
    ResponseEntity<Album> putAlbum(Album album, Long id);
    boolean deleteAlbum (long id);
}
