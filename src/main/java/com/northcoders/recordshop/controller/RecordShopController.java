package com.northcoders.recordshop.controller;

import com.northcoders.recordshop.exception.ResourceNotFoundException;
import com.northcoders.recordshop.model.Album;
import com.northcoders.recordshop.service.RecordShopService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Album> postAlbum(@RequestBody Album album){
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
}
