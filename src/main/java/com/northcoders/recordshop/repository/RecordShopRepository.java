package com.northcoders.recordshop.repository;

import com.northcoders.recordshop.model.Album;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordShopRepository extends CrudRepository<Album, Long> {
    List<Album> findByArtist (String artist);
    List<Album> findByReleaseYear (Integer year);
}
