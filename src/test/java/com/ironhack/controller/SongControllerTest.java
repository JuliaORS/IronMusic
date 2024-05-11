package com.ironhack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.dto.SongGeneralInfoDTO;
import com.ironhack.security.utils.ArtistStatus;
import com.ironhack.security.model.Artist;
import com.ironhack.security.model.Role;
import com.ironhack.security.model.User;
import com.ironhack.security.repository.ArtistRepository;
import com.ironhack.security.repository.RoleRepository;
import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.model.Song;
import com.ironhack.repository.SongRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Autowired
    private RoleRepository roleRepository;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Artist artist;

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        artist = new Artist(new User(null, "artist", "artist", "1234",
                true, ArtistStatus.ACTIVE, new ArrayList<>(), null));
        Collection<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("ROLE_USER").get());
        roles.add(roleRepository.findByName("ROLE_ARTIST").get());
        artist.setRoles(roles);
        artistRepository.save(artist);

        Song song1 =  new Song("Song title 1", "34:14", artist, null, "pop");
        Song song2 =  new Song("Song title 2", "45:14", artist, null, "pop");
        songRepository.save(song1);
        songRepository.save(song2);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(artist.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    void tearDown() {
        songRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    public void saveSongCorrectInfoTest() throws Exception{
        Song song3 =  new Song("song title 3", "3:14", artist, null, "culture");
        String songJson = objectMapper.writeValueAsString(song3);
        String expectedJson = objectMapper.writeValueAsString(new SongGeneralInfoDTO(song3));

        mockMvc.perform(post("/api/artist/song")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(songJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void saveSongWrongDurationFormatTest() throws Exception{
        Song song3 =  new Song("song title 3", "3:::14", artist, null, "culture");
        String songJson = objectMapper.writeValueAsString(song3);
        String expectedJson = objectMapper.writeValueAsString(new SongGeneralInfoDTO(song3));

        mockMvc.perform(post("/api/artist/song")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(songJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString(
                        "Bad request. Duration has not a correct format: HH:MM:SS or MM:SS or SS")));
    }

    @Test
    public void saveSongEmptyTitleTest() throws Exception{
        Song Song3 =  new Song("", "3:14", artist, null, "comedy");
        String songJson = objectMapper.writeValueAsString(Song3);
        String expectedJson = objectMapper.writeValueAsString(new SongGeneralInfoDTO(Song3));

        mockMvc.perform(post("/api/artist/song")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(songJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Bad request. Title is required.")));
    }

    @Test
    public void deleteSongByTitleTest() throws Exception{
        mockMvc.perform(delete("/api/artist/song/{title}", "song title 1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteSongByTitleWrongTitleTest() throws Exception{
        mockMvc.perform(delete("/api/artist/song/{title}", "wrong title")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString(
                        "Song with title \"wrong title\" not found")));
    }
    @Test
    void getAllSongsTest() throws Exception {
        List<Song> Songs = songRepository.findAll();
        List<SongGeneralInfoDTO> audioGeneralInfoDTOS = new ArrayList<>();
        for(Song song : Songs){
            audioGeneralInfoDTOS.add(new SongGeneralInfoDTO(song));
        }
        String expectedJson = objectMapper.writeValueAsString(audioGeneralInfoDTOS);

        mockMvc.perform(get("/api/user/songs").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }
    @Test
    void getSongsByTitleTest() throws Exception {
        List<Song> songs = songRepository.findByTitleContaining("title");
        List<SongGeneralInfoDTO> audioGeneralInfoDTOS = new ArrayList<>();
        for(Song song : songs){
            audioGeneralInfoDTOS.add(new SongGeneralInfoDTO(song));
        }
        String expectedJson = objectMapper.writeValueAsString(audioGeneralInfoDTOS);
        mockMvc.perform(get("/api/user/song/title/{title}", "title")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void getSongsByTitleNotExistingTitleTest() throws Exception {
        mockMvc.perform(get("/api/user/song/title/{title}", "wrong")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("No songs found with that title.")));
    }

    @Test
    void getSongsByArtistNameTest() throws Exception {
        List<Song> songs = songRepository.findByArtistNameContaining("artist");
        List<SongGeneralInfoDTO> audioGeneralInfoDTOS = new ArrayList<>();
        for(Song song : songs){
            audioGeneralInfoDTOS.add(new SongGeneralInfoDTO(song));
        }
        String expectedJson = objectMapper.writeValueAsString(audioGeneralInfoDTOS);
        mockMvc.perform(get("/api/user/song/artist_name/{artistName}", "artist")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void getSongsByArtistNameNotExistingArtistTest() throws Exception {
        mockMvc.perform(get("/api/user/song/artist_name/{artistName}", "wrong")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("No songs found with that artist name.")));
    }

    @Test
    void getSongsByGenreTest() throws Exception {
        List<Song> songs = songRepository.findByGenreContaining("pop");
        List<SongGeneralInfoDTO> audioGeneralInfoDTOS = new ArrayList<>();
        for(Song song : songs){
            audioGeneralInfoDTOS.add(new SongGeneralInfoDTO(song));
        }
        String expectedJson = objectMapper.writeValueAsString(audioGeneralInfoDTOS);
        mockMvc.perform(get("/api/user/song/genre/{genre}", "pop")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void getSongsByGenreNotExistingGenreTest() throws Exception {
        mockMvc.perform(get("/api/user/song/genre/{genre}", "wrong")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("No songs found with that genre.")));
    }

    @Test
    void getSongsByAllInfoTest() throws Exception {
        List<SongGeneralInfoDTO> audioGeneralInfoDTOS = new ArrayList<>();
        List<Song> songs = songRepository.findAll();
        for(Song song : songs){
            audioGeneralInfoDTOS.add(new SongGeneralInfoDTO(song));
        }
        String expectedJson = objectMapper.writeValueAsString(audioGeneralInfoDTOS);
        mockMvc.perform(get("/api/user/song/{info}", "song")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void getSongsByAllInfoWrongInfoTest() throws Exception {
        mockMvc.perform(get("/api/user/song/{info}", "wrong info")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("No songs found with that info.")));
    }
}
