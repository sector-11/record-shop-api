package com.northcoders.recordshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.northcoders.recordshop.model.Album;
import com.northcoders.recordshop.model.Genre;
import com.northcoders.recordshop.service.RecordShopService;
import com.northcoders.recordshop.service.RecordShopServiceImpl;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@SpringBootTest
class RecordShopControllerTest {

    @Mock
    private RecordShopService mockRecordShopService;

    @InjectMocks
    private RecordShopController recordShopController;

    @Autowired
    private MockMvc mockMvcController;

    @BeforeEach
    public void setup(){
        mockMvcController = MockMvcBuilders.standaloneSetup(recordShopController).build();
    }

    @Test
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
}