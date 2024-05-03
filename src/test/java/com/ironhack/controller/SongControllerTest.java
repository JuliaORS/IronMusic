package com.ironhack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.demosecurityjwt.security.models.Artist;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.repositories.ArtistRepository;
import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.model.Song;
import com.ironhack.repository.SongRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class SongControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private SongRepository songRepository;
    @Autowired
    private ArtistRepository artistRepository;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Artist artist = new Artist(new User(null, "Coldplay", "co", "1234", false, new ArrayList<>(), null));
        artistRepository.save(artist);
        Song newSong =  new Song("title", "5:13", artist, null, "rock");
        Song newSong2 =  new Song("new title 2", "3:14", artist, null, "pop");
        songRepository.saveAll(List.of(newSong, newSong2));
    }

    @AfterEach
    void tearDown() {
        songRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    void getAllSongsTest() throws Exception {
        List<Song> songs= songRepository.findAll();
        List<AudioGeneralInfoDTO> audioGeneralInfoDTOS = new ArrayList<>();
        for(Song song : songs){
            audioGeneralInfoDTOS.add(new AudioGeneralInfoDTO(song));
        }
        String expectedJson = objectMapper.writeValueAsString(audioGeneralInfoDTOS);

        mockMvc.perform(get("/api/users/songs").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }
    @Test
    void getSongsByTitleTest() throws Exception {
        List<Song> songs= songRepository.findByTitleContaining("title");
        List<AudioGeneralInfoDTO> audioGeneralInfoDTOS = new ArrayList<>();
        for(Song song : songs){
            audioGeneralInfoDTOS.add(new AudioGeneralInfoDTO(song));
        }
        String expectedJson = objectMapper.writeValueAsString(audioGeneralInfoDTOS);
        mockMvc.perform(get("/api/users/song/title/{title}", "title").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }


}
