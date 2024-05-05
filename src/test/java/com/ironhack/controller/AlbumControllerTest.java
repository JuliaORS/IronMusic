package com.ironhack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.demosecurityjwt.security.Utils.ArtistStatus;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AlbumControllerTest {
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
    private Album album;

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
        Song song1 =  new Song("song title 1", "5:13", artist, null, "rock");
        Song song2 =  new Song("song title 2", "3:14", artist, null, "pop");
        List<Song> songList = new ArrayList<>();
        songList.add(song1);
        songList.add(song2);

        album =  new Album("album title", artist, null);
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
    void tearsDown(){
        albumRepository.deleteAll();
        songRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    public void saveAlbumCorrectInfoTest () throws Exception {
        Artist newArtist = new Artist(new User(null, "julia", "ju",
                "1234", true, ArtistStatus.ACTIVE, new ArrayList<>(), null));
        Collection<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("ROLE_USER").get());
        roles.add(roleRepository.findByName("ROLE_ARTIST").get());
        newArtist.setRoles(roles);
        artistRepository.save(newArtist);

        Album newAlbum =  new Album(" new album title", null, null);
        String albumJson = objectMapper.writeValueAsString(newAlbum);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(newArtist.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        newAlbum.setArtist(newArtist);

        String expectedJson = objectMapper.writeValueAsString(new AlbumGeneralInfoDTO(newAlbum));

        mockMvc.perform(post("/api/artist/album")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(albumJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void saveAlbumEmptyTitleTest() throws Exception{
        Artist newArtist = new Artist(new User(null, "julia", "ju",
                "1234", true, ArtistStatus.ACTIVE, new ArrayList<>(), null));
        Collection<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("ROLE_USER").get());
        roles.add(roleRepository.findByName("ROLE_ARTIST").get());
        newArtist.setRoles(roles);
        artistRepository.save(newArtist);

        Album newAlbum =  new Album("", null, null);
        String albumJson = objectMapper.writeValueAsString(newAlbum);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(newArtist.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        newAlbum.setArtist(newArtist);

        String expectedJson = objectMapper.writeValueAsString(new AlbumGeneralInfoDTO(newAlbum));

        mockMvc.perform(post("/api/artist/album")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(albumJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Bad request. Title is required.")));
    }

    @Test
    public void deleteAlbumByTitleTest() throws Exception{
        mockMvc.perform(delete("/api/artist/album/{title}", "album title")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteAlbumByTitleWrongTitleTest() throws Exception{
        mockMvc.perform(delete("/api/artist/album/{title}", "wrong title")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("Album with title \"wrong title\" not found")));
    }

    @Test
    public void addSongToAlbumBytTitleSongTest() throws Exception {
        Song song3 =  new Song("song title 3", "3:14", artist, null, "pop");
        songRepository.save(song3);
        mockMvc.perform(put("/api/artist/album/{albumTitle}/song/{songTitle}", "album title", "song title 3")
                        .contentType(MediaType.APPLICATION_JSON))
                 .andExpect(status().isOk());
    }

    @Test
    public void addSongToAlbumBytTitleSongWrongAlbumTitleTest() throws Exception {
        Song song3 =  new Song("song title 3", "3:14", artist, null, "pop");
        songRepository.save(song3);
        mockMvc.perform(put("/api/artist/album/{albumTitle}/song/{songTitle}", "wrong album", "song title 3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("Album with title \"wrong album\" not found")));
    }

    @Test
    public void addSongToAlbumBytTitleSongWrongSongTitleTest() throws Exception {
        mockMvc.perform(put("/api/artist/album/{albumTitle}/song/{songTitle}", "album title", "wrong song")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("Song with title \"wrong song\" not found")));
    }

    @Test
    public void removeSongFromAlbumTest() throws Exception {
        mockMvc.perform(put("/api/artist/album/{albumTitle}/song/{songTitle}", "album title", "song title 2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void removeSongFromAlbumWrongAlbumTitleTest() throws Exception {
        mockMvc.perform(put("/api/artist/album/{albumTitle}/song/{songTitle}", "wrong album", "song title 2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("Album with title \"wrong album\" not found")));
    }

    @Test
    public void removeSongFromAlbumWrongSongTitleTest() throws Exception {
        mockMvc.perform(put("/api/artist/album/{albumTitle}/song/{songTitle}", "album title", "wrong song")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("Song with title \"wrong song\" not found")));
    }

    @Test
    void getAllAlbumsTest() throws Exception {
        List<Album> albums = albumRepository.findAll();
        List<AlbumGeneralInfoDTO> albumGeneralInfoDTOS = new ArrayList<>();
        for(Album album : albums){
            albumGeneralInfoDTOS.add(new AlbumGeneralInfoDTO(album));
        }
        String expectedJson = objectMapper.writeValueAsString(albumGeneralInfoDTOS);

        mockMvc.perform(get("/api/user/albums").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void getAlbumsByTitleTest() throws Exception {
        List<Album> albums = albumRepository.findByTitleContaining("title");
        List<AlbumGeneralInfoDTO> albumGeneralInfoDTOS = new ArrayList<>();
        for(Album album : albums){
            albumGeneralInfoDTOS.add(new AlbumGeneralInfoDTO(album));
        }
        String expectedJson = objectMapper.writeValueAsString(albumGeneralInfoDTOS);
        mockMvc.perform(get("/api/user/album/title/{title}", "title")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void getAlbumsByTitleNotExistingTitleTest() throws Exception {
        mockMvc.perform(get("/api/user/album/title/{title}", "wrong")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("No albums found with that title.")));
    }

    @Test
    void getAlbumsByArtistNameTest() throws Exception {
        List<Album> albums = albumRepository.findByArtistNameContaining("artist");
        List<AlbumGeneralInfoDTO> albumGeneralInfoDTOS = new ArrayList<>();
        for(Album album : albums){
            albumGeneralInfoDTOS.add(new AlbumGeneralInfoDTO(album));
        }
        String expectedJson = objectMapper.writeValueAsString(albumGeneralInfoDTOS);
        mockMvc.perform(get("/api/user/album/artist_name/{artistName}", "artist")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void getAlbumsByArtistNameNotExistingArtistTest() throws Exception {
        mockMvc.perform(get("/api/user/album/artist_name/{artistName}", "wrong")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("No albums found with that artist name.")));
    }


    @Test
    void getAlbumsByAllInfoTest() throws Exception {
        List<AlbumGeneralInfoDTO> albumGeneralInfoDTOS = new ArrayList<>();
        List<Album> albums = albumRepository.findAll();
        for(Album album : albums){
            albumGeneralInfoDTOS.add(new AlbumGeneralInfoDTO(album));
        }
        String expectedJson = objectMapper.writeValueAsString(albumGeneralInfoDTOS);
        mockMvc.perform(get("/api/user/album/{info}", "album")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void getAlbumsByAllInfoWrongInfoTest() throws Exception {
        mockMvc.perform(get("/api/user/album/{info}", "wrong info")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("No albums found with that info.")));
    }
}
