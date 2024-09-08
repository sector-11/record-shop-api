package com.northcoders.recordshop.service;

import com.northcoders.recordshop.exception.BadRequestException;
import com.northcoders.recordshop.exception.ResourceNotFoundException;
import com.northcoders.recordshop.model.Album;
import com.northcoders.recordshop.model.Genre;
import com.northcoders.recordshop.repository.RecordShopRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
class RecordShopServiceTests {

    @Mock
    RecordShopRepository mockRecordShopRepository;

    @InjectMocks
    RecordShopServiceImpl recordShopService;

    @Nested
    @DisplayName("getAllAlbums tests")
    class GetAllAlbumsTests {
        @Test
        @DisplayName("getAllAlbums with items in db returns the expected list of items")
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
        @DisplayName("getAllAlbums empty db throws ResourceNotFoundException")
        void TestGetAllAlbumsWhileThereAreNone() {
            List<Album> emptyAlbumList = new ArrayList<>();

            when(mockRecordShopRepository.findAll()).thenReturn(emptyAlbumList);

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> recordShopService.getAllAlbums());
        }
    }


    @Nested
    @DisplayName("getAlbumById tests")
    class GetAlbumByIdTests {
        @Test
        @DisplayName("getAlbumById with good id returns an album")
        void testGetAlbumByIdFound() {
            Album album = new Album(1L, "Testing", "Red Green Cycle", 2024, Genre.POP);

            when(mockRecordShopRepository.findById(1L)).thenReturn(Optional.of(album));

            Album result = recordShopService.getAlbumById(1L);

            assertThat(result).isEqualTo(album);
        }

        @Test
        @DisplayName("getAlbumById with id that doesn't match db entry throws ResourceNotFoundException")
        void testGetAlbumByIdNotFound() {
            when(mockRecordShopRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> recordShopService.getAlbumById(1L));
        }
    }


    @Nested
    @DisplayName("insertNewAlbum tests")
    class InsertNewAlbumTests {
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
    }


    @Nested
    @DisplayName("putAlbum tests")
    class PutAlbumTests {
        @Test
        @DisplayName("putAlbum returns the correctly changed album object with an OK status when given a valid id and body for an item in the db")
        void testPutAlbumValidIdValidBodyAndExistsInDB() {
            long id = 1L;
            Album albumToGive = new Album(null, "Stiff Little Fingers", 1979, Genre.ROCK);
            Album albumFromRepo = new Album(1L, "Inflammable Material", "Flexible Large Toes", 11979, Genre.JAZZ);
            Album expectedAlbum = new Album(1L, "Inflammable Material", "Stiff Little Fingers", 1979, Genre.ROCK);
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

            assertThrows(BadRequestException.class, () -> recordShopService.putAlbum(albumToGive, id));
        }

        @Test
        @DisplayName("putAlbum throws a BadRequestException when given a valid id for an item in the db but has an invalid body")
        void testPutAlbumValidIdInvalidBodyAndExistsInDB() {
            long id = 1L;
            Album albumToGive = null;

            when(mockRecordShopRepository.existsById(id)).thenReturn(true);

            assertThrows(BadRequestException.class, () -> recordShopService.putAlbum(albumToGive, id));
        }

        @Test
        @DisplayName("putAlbum returns the correctly changed album object and a created status when given a valid body and no id")
        void testPutAlbumNoIdValidBody() {
            Long id = null;
            Album albumToGive = new Album("Inflammable Material", "Stiff Little Fingers", 1979, Genre.ROCK);
            Album expectedAlbum = new Album(1L, "Inflammable Material", "Stiff Little Fingers", 1979, Genre.ROCK);
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

            assertThrows(BadRequestException.class, () -> recordShopService.putAlbum(albumToGive, id));
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


    @Nested
    @DisplayName("deleteAlbum tests")
    class DeleteAlbumTests {
        @Test
        @DisplayName("deleteAlbum returns true when given a valid id that exists in the db")
        void testDeleteAlbumValidIdInDb() {
            long id = 1L;

            when(mockRecordShopRepository.existsById(id)).thenReturn(true);

            boolean result = recordShopService.deleteAlbum(id);

            assertTrue(result);
        }

        @Test
        @DisplayName("deleteAlbum throws ResourceNotFoundException when given a valid id that doesn't exist in the db")
        void testDeleteAlbumValidIdNotInDb() {
            long id = 1L;

            when(mockRecordShopRepository.existsById(id)).thenReturn(false);

            assertThrows(ResourceNotFoundException.class, () -> recordShopService.deleteAlbum(id));
        }
    }


    @Nested
    @DisplayName("getAllAlbumsByArtist tests")
    class GetAllAlbumsByArtistTest {
        @Test
        @DisplayName("getAllAlbumsByArtist returns correct list of albums when given valid name of album in db")
        void testGetAllAlbumsByArtistAndPresent() {
            String artist = "Test";
            List<Album> albums = new ArrayList<>();
            albums.add(new Album("The Test pt 1", "Test", 2024, Genre.POP));
            albums.add(new Album("The Test pt 2", "Test", 2024, Genre.POP));
            albums.add(new Album("The Test pt 3", "Test", 2024, Genre.POP));

            when(mockRecordShopRepository.findByArtist(artist)).thenReturn(albums);

            List<Album> result = recordShopService.getAllAlbumsByArtist(artist);

            assertThat(result).isEqualTo(albums);
        }

        @Test
        @DisplayName("getAllAlbumsByArtist throws ResourceNotFoundException when given valid name not matching db")
        void testGetAllAlbumsByArtistAndNotPresent() {
            String artist = "Test";
            List<Album> noAlbums = new ArrayList<>();

            when(mockRecordShopRepository.findByArtist(artist)).thenReturn(noAlbums);

            assertThrows(ResourceNotFoundException.class, () -> recordShopService.getAllAlbumsByArtist(artist));
        }

        @Test
        @DisplayName("getAllAlbumsByArtist throws BadRequestException when given invalid name")
        void testGetAllAlbumsByArtistAndNullString() {
            String artist = null;

            assertThrows(BadRequestException.class, () -> recordShopService.getAllAlbumsByArtist(artist));
        }
    }


    @Nested
    @DisplayName("getAllAlbumsByReleaseYear tests")
    class GetAllAlbumsByReleaseYearTest {
        @Test
        @DisplayName("getAllAlbumsByReleaseYear returns correct list of albums when given valid release year of album in db")
        void testGetAllAlbumsByReleaseYearAndPresent() {
            Integer year = 2024;
            List<Album> albums = new ArrayList<>();
            albums.add(new Album("The Test pt 1", "Test", 2024, Genre.POP));
            albums.add(new Album("The Test pt 2", "Test", 2024, Genre.POP));
            albums.add(new Album("The Test pt 3", "Test", 2024, Genre.POP));

            when(mockRecordShopRepository.findByReleaseYear(year)).thenReturn(albums);

            List<Album> result = recordShopService.getAllAlbumsByReleaseYear(year);

            assertThat(result).isEqualTo(albums);
        }

        @Test
        @DisplayName("getAllAlbumsByReleaseYear throws ResourceNotFoundException when given valid year not matching db entry")
        void testGetAllAlbumsByReleaseYearAndNotPresent() {
            Integer year = 2024;
            List<Album> noAlbums = new ArrayList<>();

            when(mockRecordShopRepository.findByReleaseYear(year)).thenReturn(noAlbums);

            assertThrows(ResourceNotFoundException.class, () -> recordShopService.getAllAlbumsByReleaseYear(year));
        }

        @Test
        @DisplayName("getAllAlbumsByReleaseYear throws BadRequestException when given invalid year")
        void testGetAllAlbumsByReleaseYearAndNullInteger() {
            Integer year = null;

            assertThrows(BadRequestException.class, () -> recordShopService.getAllAlbumsByReleaseYear(year));
        }
    }


    @Nested
    @DisplayName("getAllAlbumsByGenre tests")
    class GetAllAlbumsByGenre {
        @Test
        @DisplayName("getAllAlbumsByGenre returns correct list of albums when given valid genre of albums in db")
        void testGetAllAlbumsByGenreAndPresent() {
            Genre genre = Genre.POP;
            List<Album> albums = new ArrayList<>();
            albums.add(new Album("The Test pt 1", "Test", 2024, Genre.POP));
            albums.add(new Album("The Test pt 2", "Test", 2024, Genre.POP));
            albums.add(new Album("The Test pt 3", "Test", 2024, Genre.POP));

            when(mockRecordShopRepository.findByGenre(genre)).thenReturn(albums);

            List<Album> result = recordShopService.getAllAlbumsByGenre(genre);

            assertThat(result).isEqualTo(albums);
        }

        @Test
        @DisplayName("getAllAlbumsByGenre throws ResourceNotFoundException when given valid genre not matching any db entry")
        void testGetAllAlbumsByGenreAndNotPresent() {
            Genre genre = Genre.POP;
            List<Album> noAlbums = new ArrayList<>();

            when(mockRecordShopRepository.findByGenre(genre)).thenReturn(noAlbums);

            assertThrows(ResourceNotFoundException.class, () -> recordShopService.getAllAlbumsByGenre(genre));
        }

        @Test
        @DisplayName("getAllAlbumsByGenre throws BadRequestException when given invalid genre")
        void testGetAllAlbumsByGenreAndNullGenre() {
            Genre genre = null;

            assertThrows(BadRequestException.class, () -> recordShopService.getAllAlbumsByGenre(genre));
        }
    }


    @Nested
    @DisplayName("getAllAlbumsByName tests")
    class GetAllAlbumsByNameTest {
        @Test
        @DisplayName("getAllAlbumsByName returns correct list of albums when given valid name of an album in db")
        void testGetAllAlbumsByNameAndPresent() {
            String name = "The Test";
            List<Album> albums = new ArrayList<>();
            albums.add(new Album("The Test", "Test", 2024, Genre.POP));

            when(mockRecordShopRepository.findByAlbumName(name)).thenReturn(albums);

            List<Album> result = recordShopService.getAllAlbumsByName(name);

            assertThat(result).isEqualTo(albums);
        }

        @Test
        @DisplayName("getAllAlbumsByName throws ResourceNotFoundException when given valid name not matching any db entry")
        void testGetAllAlbumsByNameAndNotPresent() {
            String name = "The Test";
            List<Album> noAlbums = new ArrayList<>();

            when(mockRecordShopRepository.findByAlbumName(name)).thenReturn(noAlbums);

            assertThrows(ResourceNotFoundException.class, () -> recordShopService.getAllAlbumsByName(name));
        }

        @Test
        @DisplayName("getAllAlbumsByGenre throws BadRequestException when given invalid name")
        void testGetAllAlbumsByNameAndNullString() {
            String name = null;

            assertThrows(BadRequestException.class, () -> recordShopService.getAllAlbumsByName(name));
        }
    }
}