package com.ironhack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.demosecurityjwt.security.models.Artist;
import com.ironhack.demosecurityjwt.security.models.Role;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.repositories.ArtistRepository;
import com.ironhack.demosecurityjwt.security.repositories.RoleRepository;
import com.ironhack.dto.AlbumGeneralInfoDTO;
import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.model.Album;
import com.ironhack.model.Song;
import com.ironhack.repository.AlbumRepository;
import com.ironhack.repository.SongRepository;
import jakarta.validation.Valid;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    private AlbumRepository albumRepository;
    @Autowired
    private RoleRepository roleRepository;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Artist artist;
    private List<Song> songList = new ArrayList<>();

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        artist = new Artist(new User(null, "artist", "artist", "1234", true, new ArrayList<>(), null));
        Collection<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("ROLE_USER"));
        roles.add(roleRepository.findByName("ROLE_ARTIST"));
        artist.setRoles(roles);

        artistRepository.save(artist);
        Song song1 =  new Song("song title 1", "5:13", artist, null, "rock");
        Song song2 =  new Song("song title 2", "3:14", artist, null, "pop");

        songList.add(song1);
        songList.add(song2);

        Album album =  new Album("album title", artist, null);
        album.getSongs().add(song1);
        album.getSongs().add(song2);
        albumRepository.save(album);
        song1.setAlbum(album);
        song2.setAlbum(album);
        songRepository.saveAll(songList);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(artist.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    void tearDown() {
        albumRepository.deleteAll();
        songRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    public void saveSongCorrectInfoTest() throws Exception{
        Song song3 =  new Song("song title 3", "3:14", artist, null, "pop");
        String albumJson = objectMapper.writeValueAsString(song3);
        String expectedJson = objectMapper.writeValueAsString(new AudioGeneralInfoDTO(song3));

        mockMvc.perform(post("/api/artist/song")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(albumJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void saveSongWrongDurationFormatTest() throws Exception{
        Song song3 =  new Song("song title 3", "3:::14", artist, null, "pop");
        String albumJson = objectMapper.writeValueAsString(song3);
        String expectedJson = objectMapper.writeValueAsString(new AudioGeneralInfoDTO(song3));

        mockMvc.perform(post("/api/artist/song")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(albumJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Bad request. Duration has not a correct format: HH:MM:SS or MM:SS or SS")));
    }

    @Test
    public void saveSongEmptyTitleTest() throws Exception{
        Song song3 =  new Song("", "3:14", artist, null, "pop");
        String albumJson = objectMapper.writeValueAsString(song3);
        String expectedJson = objectMapper.writeValueAsString(new AudioGeneralInfoDTO(song3));

        mockMvc.perform(post("/api/artist/song")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(albumJson))
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
                .andExpect(content().string(Matchers.containsString("Song with title \"wrong title\" not found")));
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
        mockMvc.perform(get("/api/users/song/title/{title}", "title")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    void getSongsByTitleNotExistingTitleTest() throws Exception {
        mockMvc.perform(get("/api/users/song/title/{title}", "wrong")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("No songs found with that title.")));
    }

    @Test
    void getSongsByArtistNameTest() throws Exception {
        List<Song> songs= songRepository.findByArtistNameContaining("artist");
        List<AudioGeneralInfoDTO> audioGeneralInfoDTOS = new ArrayList<>();
        for(Song song : songs){
            audioGeneralInfoDTOS.add(new AudioGeneralInfoDTO(song));
        }
        String expectedJson = objectMapper.writeValueAsString(audioGeneralInfoDTOS);
        mockMvc.perform(get("/api/users/song/artist_name/{artistName}", "artist")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void getSongsByArtistNameNotExistingArtistTest() throws Exception {
        mockMvc.perform(get("/api/users/song/artist_name/{artistName}", "wrong")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("No songs found with that artist name.")));
    }

    @Test
    void getSongsByGenreTest() throws Exception {
        List<Song> songs= songRepository.findByGenreContaining("pop");
        List<AudioGeneralInfoDTO> audioGeneralInfoDTOS = new ArrayList<>();
        for(Song song : songs){
            audioGeneralInfoDTOS.add(new AudioGeneralInfoDTO(song));
        }
        String expectedJson = objectMapper.writeValueAsString(audioGeneralInfoDTOS);
        mockMvc.perform(get("/api/users/song/genre/{genre}", "pop")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void getSongsByGenreNotExistingGenreTest() throws Exception {
        mockMvc.perform(get("/api/users/song/genre/{genre}", "wrong")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("No songs found with that genre.")));
    }

    @Test
    void getSongsByAllInfoTest() throws Exception {
        List<AudioGeneralInfoDTO> audioGeneralInfoDTOS = new ArrayList<>();
        for(Song song : songList){
            audioGeneralInfoDTOS.add(new AudioGeneralInfoDTO(song));
        }
        String expectedJson = objectMapper.writeValueAsString(audioGeneralInfoDTOS);
        mockMvc.perform(get("/api/users/song/{info}", "album")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void getSongsByAllInfoWrongInfoTest() throws Exception {
        mockMvc.perform(get("/api/users/song/{info}", "wrong info")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("No songs found with that info.")));
    }
}
