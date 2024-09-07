package com.northcoders.recordshop.controller;

import com.northcoders.recordshop.exception.BadRequestException;
import com.northcoders.recordshop.exception.ResourceNotFoundException;
import com.northcoders.recordshop.model.Album;
import com.northcoders.recordshop.service.RecordShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/record-shop")
public class RecordShopController {
    @Autowired
    RecordShopService recordShopService;

    @GetMapping("/records")
    public ResponseEntity<List<Album>> getAllAlbums(){
        List<Album> albumList = recordShopService.getAllAlbums();
        return ResponseEntity.ok(albumList);
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
        if (id == null) throw new ResourceNotFoundException("No id supplied! You must supply an id to search for on this endpoint!");
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
