package com.northcoders.recordshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.northcoders.recordshop.exception.ResourceNotFoundException;
import com.northcoders.recordshop.model.Album;
import com.northcoders.recordshop.model.Genre;
import com.northcoders.recordshop.service.RecordShopService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@AutoConfigureMockMvc
@SpringBootTest
class RecordShopControllerTest {

    @Mock
    private RecordShopService mockRecordShopService;

    @InjectMocks
    private RecordShopController recordShopController;

    @Autowired
    private MockMvc mockMvcController;

    private ObjectMapper mapper;

    @BeforeEach
    public void setup(){
        mockMvcController = MockMvcBuilders.standaloneSetup(recordShopController).build();
        mapper = new ObjectMapper();
    }

    @Test
    @DisplayName("GET request to /records endpoint gives OK status and an expected list of albums assuming valid service layer")
    public void testGetAllAlbumsReturnsAllAlbums() throws Exception {
        //Test data
        List<Album> albumList = new ArrayList<>();
        albumList.add(new Album(1L, "Black Sabbath", "Black Sabbath", 1970, Genre.METAL));
        albumList.add(new Album(2L, "If You Can Believe Your Eyes and Ears", "The Mamas and the Papas", 1966, Genre.ROCK));
        albumList.add(new Album(3L, "Mm..Food", "MF DOOM", 2004, Genre.HIPHOP));
        albumList.add(new Album(4L, "Voulez-Vous", "ABBA", 1979, Genre.POP));

        //Mock method needed from other layer
        when(mockRecordShopService.getAllAlbums()).thenReturn(albumList);

        //Test endpoint
        this.mockMvcController.perform(
                MockMvcRequestBuilders.get("/api/v1/record-shop/records"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].albumName").value("Black Sabbath"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].artist").value("The Mamas and the Papas"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(3L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].releaseYear").value(2004))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].id").value(4L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].genre").value("Pop"));
    }

    @Test
    @DisplayName("GET request to /records/ endpoint with a valid id gives OK response and a single expected album assuming valid service layer")
    public void testGetAlbumByIdReturnsAlbum() throws Exception {
        Album album = new Album(1L, "Testing", "Red Green Cycle", 2024, Genre.POP);

        when(mockRecordShopService.getAlbumById(1L)).thenReturn(album);

        this.mockMvcController.perform(
                MockMvcRequestBuilders.get("/api/v1/record-shop/records/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.albumName").value("Testing"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.artist").value("Red Green Cycle"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseYear").value(2024))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value("Pop"));
    }

    @Test
    @DisplayName("GET request to /records/ endpoint without id throws ResourceNotFoundException")
    public void testGetAlbumByIdNoParamThrowsException() throws Exception {
        ServletException exception = assertThrows(ServletException.class, () -> this.mockMvcController.perform(
                MockMvcRequestBuilders.get("/api/v1/record-shop/records/")));

        assertEquals(exception.getRootCause().getClass(), ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("POST request to /records endpoint gives CREATED status assuming valid service layer")
    public void testPostAlbum() throws Exception {
        Album album = new Album("Mm..Food", "MF DOOM", 2004, Genre.HIPHOP);
        Album expectedAlbum = new Album(1L,"Mm..Food", "MF DOOM", 2004, Genre.HIPHOP);

        when(mockRecordShopService.insertNewAlbum(any(Album.class))).thenReturn(expectedAlbum); //has to accept any album here as mapper is changing the data in test environment, for some reason. this does not occur when running normally.

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.post("/api/v1/record-shop/records")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsBytes(album)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(mockRecordShopService, times(1)).insertNewAlbum(any(Album.class));
    }

    @Test
    @DisplayName("PUT request to /records/{id} gives OK status assuming valid service layer and input")
    public void testPutAlbum() throws Exception {
        long id = 1L;
        Album album = new Album(0L,"Mm..Food", "MF DOOM", 2004, Genre.HIPHOP);
        Album albumAfterMapping = new Album(0L,"Mm..Food", "MF DOOM", 2004, Genre.HIPHOP);
        Album expectedAlbum = new Album(1L,"Mm..Food", "MF DOOM", 2004, Genre.HIPHOP);
        ResponseEntity<Album> expectedResponse = ResponseEntity.ok(expectedAlbum);

        when(mockRecordShopService.putAlbum(albumAfterMapping, id)).thenReturn(expectedResponse);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.put("/api/v1/record-shop/records/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsBytes(album)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("PUT request to /records/ gives CREATED status assuming valid service layer and input")
    public void testPutAlbumNoID() throws Exception {
        Long id = null;
        Album album = new Album(0L,"Mm..Food", "MF DOOM", 2004, Genre.HIPHOP);
        Album albumAfterMapping = new Album(0L,"Mm..Food", "MF DOOM", 2004, Genre.HIPHOP);
        Album expectedAlbum = new Album(1L,"Mm..Food", "MF DOOM", 2004, Genre.HIPHOP);
        ResponseEntity<Album> expectedResponse = ResponseEntity.created(URI.create("/api/v1/record-shop/records/1")).body(expectedAlbum);

        when(mockRecordShopService.putAlbum(albumAfterMapping, id)).thenReturn(expectedResponse);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.put("/api/v1/record-shop/records/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsBytes(album)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));
    }
}