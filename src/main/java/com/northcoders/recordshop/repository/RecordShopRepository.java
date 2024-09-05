package com.northcoders.recordshop.repository;

import com.northcoders.recordshop.model.Album;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordShopRepository extends CrudRepository<Album, Long> {
}
