package com.northcoders.recordshop.service;

import com.northcoders.recordshop.exception.BadRequestException;
import com.northcoders.recordshop.exception.ResourceNotFoundException;
import com.northcoders.recordshop.model.Album;
import com.northcoders.recordshop.repository.RecordShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecordShopServiceImpl implements RecordShopService{
    @Autowired
    RecordShopRepository recordShopRepository;

    @Override
    public List<Album> getAllAlbums() {
        List<Album> albumList = new ArrayList<>();
        recordShopRepository.findAll().forEach(albumList::add);
        if (albumList.isEmpty()) throw new ResourceNotFoundException("There are no albums in the database!");
        return albumList;
    }

    @Override
    public Album getAlbumById(long id) {
        Optional<Album> album = recordShopRepository.findById(id);
        if (album.isEmpty()) throw new ResourceNotFoundException("There is no album with id '" + id + "' in the database!");
        return album.get();
    }

    @Override
    public Album insertNewAlbum(Album album) {
        if (album == null || album.getAlbumName() == null || album.getArtist() == null || album.getGenre() == null || album.getReleaseYear() == null) throw new BadRequestException("You must provide an album with all fields except id filled!");
        if (album.getId() != null) throw new BadRequestException("You must not provide an id when posting new albums! The id will be set automatically by the database.");

        return recordShopRepository.save(album);
    }

    @Override
    public ResponseEntity<Album> putAlbum(Album album) {
        return null;
    }
}
