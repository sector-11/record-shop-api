package com.northcoders.recordshop.service;

import com.northcoders.recordshop.exception.ResourceNotFoundException;
import com.northcoders.recordshop.model.Album;
import com.northcoders.recordshop.model.Genre;
import com.northcoders.recordshop.repository.RecordShopRepository;
import org.apache.coyote.BadRequestException;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Test
    void testGetAlbumByIdFound() {
        Album album = new Album(1L, "Testing", "Red Green Cycle", 2024, Genre.POP);

        when(mockRecordShopRepository.findById(1L)).thenReturn(Optional.of(album));

        Album result = recordShopService.getAlbumById(1L);

        assertThat(result).isEqualTo(album);
    }

    @Test
    void testGetAlbumByIdNotFound() {
        when(mockRecordShopRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> recordShopService.getAlbumById(1L));
    }

    @Test
    void testInsertNewAlbumWithGoodInput() {
        Album album = new Album("Testing", "Red Green Cycle", 2024, Genre.POP);
        Album expectedAlbum = new Album(1L, "Testing", "Red Green Cycle", 2024, Genre.POP);

        when(mockRecordShopRepository.save(album)).thenReturn(expectedAlbum);

        Album result = recordShopService.insertNewAlbum(album);

        assertThat(result).isEqualTo(expectedAlbum);
    }

    @Test
    void testInsertNewAlbumWithBadInput() {
        Album badAlbum = new Album(1L, "Nostalgia Critic's The Wall", "Doug Walker", 2019, Genre.ROCK);
        assertThrows(BadRequestException.class, () -> recordShopService.insertNewAlbum(badAlbum));
        assertThrows(BadRequestException.class, () -> recordShopService.insertNewAlbum(null));
    }
}