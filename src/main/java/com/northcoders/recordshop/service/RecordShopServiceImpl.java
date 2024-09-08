package com.northcoders.recordshop.service;

import com.northcoders.recordshop.exception.BadRequestException;
import com.northcoders.recordshop.exception.ResourceNotFoundException;
import com.northcoders.recordshop.model.Album;
import com.northcoders.recordshop.repository.RecordShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        if (album == null || album.anyFieldOtherThanIdAIsNull()) throw new BadRequestException("You must provide an album with all fields except id filled!");
        if (album.getId() != null) throw new BadRequestException("You must not provide an id when posting new albums! The id will be set automatically by the database.");

        return recordShopRepository.save(album);
    }

    @Override
    public ResponseEntity<Album> putAlbum(Album album, Long id) {
        if (album == null) throw new BadRequestException("You must provide an album when making this request!");

        if (id == null){
            if (album.anyFieldOtherThanIdAIsNull()) throw new BadRequestException("You must provide an album with all fields except id filled when making a PUT request without an id specified on the endpoint!");
            if (album.getId() != null) throw new BadRequestException("You must not provide an id in the album when making a PUT request without an id specified on the endpoint! The id will be set automatically by the database.");

            Album createdAlbum = recordShopRepository.save(album);
            return new ResponseEntity<Album>(createdAlbum, HttpStatus.CREATED);
        } else{
            if (!recordShopRepository.existsById(id)) throw new BadRequestException("There is no album matching id '" + id + "' in the database.");

            if (album.getId() != null){
                if (!album.getId().equals(id)) throw new BadRequestException("When providing an id both in the body of your PUT request and on the endpoint, they must match! Yours were '" + album.getId() + "' in the body, and '" + id + "' on the endpoint.");
            }

            Album albumToModify = getAlbumById(id);

            if (album.getAlbumName() != null) albumToModify.setAlbumName(album.getAlbumName());
            if (album.getArtist() != null) albumToModify.setArtist(album.getArtist());
            if (album.getGenre() != null) albumToModify.setGenre(album.getGenreAsGenre());
            if (album.getReleaseYear() != null) albumToModify.setReleaseYear(album.getReleaseYear());

            Album albumToReturn = recordShopRepository.save(albumToModify);

            return ResponseEntity.ok(albumToReturn);
        }
    }

    @Override
    public boolean deleteAlbum(long id) {
        if (recordShopRepository.existsById(id)) {
            recordShopRepository.deleteById(id);
            return true;
        } else {
            throw new ResourceNotFoundException("No album found at id '" + id + "' in database.");
        }
    }

    @Override
    public List<Album> getAllAlbumsByArtist(String artist) {
        if (artist == null || artist.isEmpty()) throw new BadRequestException("An artist must be provided when trying to find all albums by an artist!");

        List<Album> resultList = recordShopRepository.findByArtist(artist);

        if (resultList.isEmpty()) throw new ResourceNotFoundException("No albums found by artist '" + artist + "' in the database!");

        return resultList;
    }

    @Override
    public List<Album> getAllAlbumsByReleaseYear(Integer year) {
        return null;
    }
}
