package com.northcoders.recordshop.service;

import com.northcoders.recordshop.model.Album;

import java.util.List;

public interface RecordShopService {
    List<Album> getAllAlbums();
    Album getAlbumById(long id);
    Album insertNewAlbum(Album album);
}
