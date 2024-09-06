package com.northcoders.recordshop.service;

import com.northcoders.recordshop.exception.ResourceNotFoundException;
import com.northcoders.recordshop.model.Album;
import com.northcoders.recordshop.model.Genre;
import com.northcoders.recordshop.repository.RecordShopRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class RecordShopServiceTests {

    @Mock
    RecordShopRepository mockRecordShopRepository;

    @InjectMocks
    RecordShopServiceImpl recordShopService;



    @Test
    void TestGetAllAlbums() {
        //Test data
        List<Album> albumList = new ArrayList<>();
        albumList.add(new Album("Black Sabbath", "Black Sabbath", 1970, Genre.METAL));
        albumList.add(new Album("If You Can Believe Your Eyes and Ears", "The Mamas and the Papas", 1966, Genre.ROCK));
        albumList.add(new Album("Mm..Food", "MF DOOM", 2004, Genre.HIPHOP));
        albumList.add(new Album("Voulez-Vous", "ABBA", 1979, Genre.POP));

        //Mock method needed from other layer
        when(mockRecordShopRepository.findAll()).thenReturn(albumList);

        //Act
        List<Album> result = recordShopService.getAllAlbums();

        //Assert
        assertThat(result).hasSize(4);
        assertThat(result).isEqualTo(albumList);
    }

    @Test
    void TestGetAllAlbumsWhileThereAreNone() {
        List<Album> emptyAlbumList = new ArrayList<>();

        when(mockRecordShopRepository.findAll()).thenReturn(emptyAlbumList);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> recordShopService.getAllAlbums());
    }
}