package com.northcoders.recordshop.service;

import com.northcoders.recordshop.exception.BadRequestException;
import com.northcoders.recordshop.exception.ResourceNotFoundException;
import com.northcoders.recordshop.model.Album;
import com.northcoders.recordshop.model.Genre;
import com.northcoders.recordshop.repository.RecordShopRepository;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
    @DisplayName("findAll with items in db returns the expected list of items")
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
    @DisplayName("findAll empty db throws ResourceNotFoundException")
    void TestGetAllAlbumsWhileThereAreNone() {
        List<Album> emptyAlbumList = new ArrayList<>();

        when(mockRecordShopRepository.findAll()).thenReturn(emptyAlbumList);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> recordShopService.getAllAlbums());
    }

    @Test
    @DisplayName("findById with good id returns an album")
    void testGetAlbumByIdFound() {
        Album album = new Album(1L, "Testing", "Red Green Cycle", 2024, Genre.POP);

        when(mockRecordShopRepository.findById(1L)).thenReturn(Optional.of(album));

        Album result = recordShopService.getAlbumById(1L);

        assertThat(result).isEqualTo(album);
    }

    @Test
    @DisplayName("findById with id that doesn't match db entry throws ResourceNotFoundException")
    void testGetAlbumByIdNotFound() {
        when(mockRecordShopRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> recordShopService.getAlbumById(1L));
    }

    @Test
    @DisplayName("insertNewAlbum with correctly formatted albums returns album with id given by db")
    void testInsertNewAlbumWithGoodInput() {
        Album album = new Album("Testing", "Red Green Cycle", 2024, Genre.POP);
        Album expectedAlbum = new Album(1L, "Testing", "Red Green Cycle", 2024, Genre.POP);

        when(mockRecordShopRepository.save(album)).thenReturn(expectedAlbum);

        Album result = recordShopService.insertNewAlbum(album);

        assertThat(result).isEqualTo(expectedAlbum);
    }

    @Test
    @DisplayName("insertNewAlbum with badly formatted albums throws BadRequestException")
    void testInsertNewAlbumWithBadInput() {
        Album badAlbum = new Album(1L, "Nostalgia Critic's The Wall", "Doug Walker", 2019, Genre.ROCK);
        assertThrows(BadRequestException.class, () -> recordShopService.insertNewAlbum(badAlbum));
        assertThrows(BadRequestException.class, () -> recordShopService.insertNewAlbum(null));
    }

    @Test
    @DisplayName("putAlbum returns the correctly changed album object with an OK status when given a valid id and body for an item in the db")
    void testPutAlbumValidIdValidBodyAndExistsInDB() {
        long id = 1L;
        Album albumToGive = new Album(null, "Stiff Little Fingers", 1979, Genre.ROCK);
        Album albumFromRepo = new Album(1L,"Inflammable Material", "Flexible Large Toes", 11979, Genre.JAZZ);
        Album expectedAlbum = new Album(1L,"Inflammable Material", "Stiff Little Fingers", 1979, Genre.ROCK);
        ResponseEntity<Album> expectedResult = new ResponseEntity<>(expectedAlbum, HttpStatus.OK);

        when(mockRecordShopRepository.existsById(id)).thenReturn(true);
        when(mockRecordShopRepository.findById(id)).thenReturn(Optional.of(albumFromRepo));
        when(mockRecordShopRepository.save(expectedAlbum)).thenReturn(expectedAlbum);

        ResponseEntity<Album> result = recordShopService.putAlbum(albumToGive, id);

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("putAlbum throws a BadRequestException when given a valid id for an item that is not the db")
    void testPutAlbumValidIdValidBodyButDoesNotExistInDB() {
        long id = 1L;
        Album albumToGive = new Album("Inflammable Material", "Stiff Little Fingers", 1979, Genre.ROCK);

        when(mockRecordShopRepository.existsById(id)).thenReturn(false);

        assertThrows(BadRequestException.class,() -> recordShopService.putAlbum(albumToGive, id));
    }

    @Test
    @DisplayName("putAlbum throws a BadRequestException when given a valid id for an item in the db but has an invalid body")
    void testPutAlbumValidIdInvalidBodyAndExistsInDB() {
        long id = 1L;
        Album albumToGive = null;

        when(mockRecordShopRepository.existsById(id)).thenReturn(true);

        assertThrows(BadRequestException.class,() -> recordShopService.putAlbum(albumToGive, id));
    }

    @Test
    @DisplayName("putAlbum returns the correctly changed album object and a created status when given a valid body and no id")
    void testPutAlbumNoIdValidBody() {
        Long id = null;
        Album albumToGive = new Album("Inflammable Material", "Stiff Little Fingers", 1979, Genre.ROCK);
        Album expectedAlbum = new Album(1L,"Inflammable Material", "Stiff Little Fingers", 1979, Genre.ROCK);
        ResponseEntity<Album> expectedResult = new ResponseEntity<>(HttpStatus.CREATED);

        when(mockRecordShopRepository.existsById(id)).thenReturn(false);

        ResponseEntity<Album> result = recordShopService.putAlbum(albumToGive, id);

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("putAlbum throws BadRequestException when given a valid body but the given id does not exist in the db")
    void testPutAlbumInvalidIdValidBody() {
        long id = 1L;
        Album albumToGive = new Album("Inflammable Material", "Stiff Little Fingers", 1979, Genre.ROCK);

        when(mockRecordShopRepository.existsById(id)).thenReturn(false);

        assertThrows(BadRequestException.class, () -> recordShopService.putAlbum(albumToGive, id));
    }

    @Test
    @DisplayName("putAlbum throws a BadRequestException when given both an invalid id and body")
    void testPutAlbumNoIdInvalidBody() {
        Long id = null;
        Album albumToGive = null;

        when(mockRecordShopRepository.existsById(id)).thenReturn(false);

        assertThrows(BadRequestException.class,() -> recordShopService.putAlbum(albumToGive, id));
    }


    @Test
    @DisplayName("putAlbum throws BadRequestException when given a valid id, which exists in the db, and body but the id given and the id in the body mismatch")
    void testPutAlbumValidIdValidBodyButMismatch() {
        long id = 1L;
        Album albumToGive = new Album(2L, "Inflammable Material", "Stiff Little Fingers", 1979, Genre.ROCK);

        when(mockRecordShopRepository.existsById(id)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> recordShopService.putAlbum(albumToGive, id));
    }
}