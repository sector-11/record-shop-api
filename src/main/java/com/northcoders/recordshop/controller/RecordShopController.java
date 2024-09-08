package com.northcoders.recordshop.controller;

import com.northcoders.recordshop.exception.BadRequestException;
import com.northcoders.recordshop.model.Album;
import com.northcoders.recordshop.model.Genre;
import com.northcoders.recordshop.service.RecordShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/record-shop")
public class RecordShopController {
    @Autowired
    RecordShopService recordShopService;

    @GetMapping("/records")
    public ResponseEntity<List<Album>> getAllAlbums(@RequestParam Map<String, String> params,
                                                    @RequestParam(name = "artist", required = false) String artist,
                                                    @RequestParam(name = "releaseYear", required = false) Integer year,
                                                    @RequestParam(name = "genre", required = false) String genreString,
                                                    @RequestParam(name = "albumName", required = false) String albumName) {
        List<Album> albumList;
        Genre genre = null;

        if (genreString != null) {
            try {
                genre = Genre.valueOf(genreString.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Provided argument '" + genreString + "' is not a valid genre!");
            }
        }

        switch (params.size()) {
            case 0 -> albumList = recordShopService.getAllAlbums();
            case 1 -> albumList = getAllAlbumsWithOneParam(params, artist, year, genre, albumName);
            default -> albumList = recordShopService.getAllAlbumsByMultipleParams(params);
        }
        return ResponseEntity.ok(albumList);
    }

    public List<Album> getAllAlbumsWithOneParam(Map<String, String> param, String artist, Integer year, Genre genre, String albumName){
        List<Album> albumList;
        if (artist != null) {
            albumList = recordShopService.getAllAlbumsByArtist(artist);
        } else if (year != null) {
            albumList = recordShopService.getAllAlbumsByReleaseYear(year);
        } else if (genre != null) {
            albumList = recordShopService.getAllAlbumsByGenre(genre);
        } else if (albumName != null) {
            albumList = recordShopService.getAllAlbumsByName(albumName);
        } else {
            String givenParam = param.keySet().toString();
            throw new BadRequestException("Given parameter '" + givenParam.substring(1, givenParam.length() - 1) + "' is not valid on this endpoint!");
        }
        return albumList;
    }

    @PostMapping("/records")
    public ResponseEntity<Album> postAlbum(@RequestBody(required = false) Album album){
        Album newAlbum = recordShopService.insertNewAlbum(album);
        return ResponseEntity.created(
                URI.create("/records/" + newAlbum.getId().toString()))
                .body(newAlbum);
    }

    @GetMapping(value = {"/records/{id}", "/records/"})
    public ResponseEntity<Album> getAlbumById(@PathVariable(required = false, name = "id") Long id){
        if (id == null) throw new BadRequestException("No id supplied! You must supply an id to search for on this endpoint!");
        Album album = recordShopService.getAlbumById(id);
        return ResponseEntity.ok(album);
    }

    @PutMapping(value = {"/records/{id}", "/records/"})
    public ResponseEntity<Album> putAlbum(@PathVariable(required = false, name = "id") Long id, @RequestBody(required = false) Album album){
        ResponseEntity<Album> response = recordShopService.putAlbum(album, id);
        if (response.getStatusCode().equals(HttpStatus.CREATED)) {
            response = ResponseEntity.created(URI.create("/api/v1/record-shop/records/" + id)).body(response.getBody());
        }
        return response;
    }

    @DeleteMapping(value = {"/records/{id}", "/records/"})
    public ResponseEntity<Album> deleteAlbum(@PathVariable(required = false, name = "id") Long id){
        if (id == null) throw new BadRequestException("No id supplied! You must supply an id to delete for on this endpoint!");
        boolean response = recordShopService.deleteAlbum(id);
        if (response) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
