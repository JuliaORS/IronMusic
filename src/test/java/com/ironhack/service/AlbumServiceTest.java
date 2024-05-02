package com.ironhack.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.demosecurityjwt.security.models.Artist;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.repositories.ArtistRepository;
import com.ironhack.dto.AlbumGeneralInfoDTO;
import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.exceptions.BadRequestFormatException;
import com.ironhack.exceptions.ResourceNotFoundException;
import com.ironhack.model.Album;
import com.ironhack.model.Podcast;
import com.ironhack.model.Song;
import com.ironhack.repository.AlbumRepository;
import com.ironhack.repository.SongRepository;
import com.ironhack.service.impl.AlbumService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AlbumServiceTest {
    @Autowired
    private AlbumService albumService;
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private SongRepository songRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp(){
        Artist artist = new Artist(new User(null, "artist", "artist", "1234", true, new ArrayList<>(), null));
        artistRepository.save(artist);
        Song song1 =  new Song("title", "5:13", artist, null, "rock");
        Song song2 =  new Song("new title 2", "3:14", artist, null, "pop");
        List<Song> songList = new ArrayList<>();
        songList.add(song1);
        songList.add(song2);
        songRepository.saveAll(songList);

        Album album =  new Album("album title", artist, songList);
        albumRepository.save(album);
    }

    @AfterEach
    void tearsDown(){
        albumRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    public void saveAlbumCorrectInfoTest() throws Exception {
        Artist artist = new Artist();
        artist.setUsername("julia");
        artistRepository.save(artist);

        Song song =  new Song("title", "3:14", artist, null, "pop");
        List<Song> songList = new ArrayList<>();
        songList.add(song);
        songRepository.save(song);

        Album album =  new Album("album title", null, songList);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(artist.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        album.setArtist(artist);

        String expectedJson = objectMapper.writeValueAsString(new AlbumGeneralInfoDTO(album));
        AlbumGeneralInfoDTO result = albumService.saveAlbum(album);
        String resultJson = objectMapper.writeValueAsString(result);

        assertEquals(2, albumRepository.count());
        assertEquals(expectedJson, resultJson);
    }

    @Test
    public void saveAlbumEmptyTitleTest() throws Exception {
        Album album =  new Album("", null, null);
        Artist artist = new Artist();
        artist.setUsername("julia");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(artist.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        artistRepository.save(artist);
        album.setArtist(artist);

        assertThrows(ConstraintViolationException.class, () -> {albumService.saveAlbum(album);});
    }

    @Test
    public void deleteAlbumExistingTitleTest(){
        Artist artist = new Artist();
        artist.setName("julia");
        artist.setUsername("julia");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(artist.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        artistRepository.save(artist);

        Song song =  new Song("hello", "3:14", artist, null, "pop");
        List<Song> songList = new ArrayList<>();
        songRepository.save(song);

        Album album =  new Album("album title", artist, songList);
        album.getSongs().add(song);
        albumRepository.save(album);
        String title = album.getTitle();

        assertFalse(albumRepository.findByTitleAndArtistUsername(title, artist.getUsername()).isEmpty());
        assertEquals(2, albumRepository.count());
        assertFalse(songRepository.findByTitleContaining("hello").isEmpty());

        assertEquals(1, songRepository.findByTitleContaining("hello").size());
        albumService.deleteAlbumByTitle(title);
        assertFalse(albumRepository.findById(album.getId()).isPresent());
        assertEquals(1, albumRepository.count());
        assertEquals(1, songRepository.findByTitleContaining("hello").size());

    }

    @Test
    public void deleteAlbumNotExistingIdTest(){
        Artist artist = new Artist();
        artist.setName("julia");
        artist.setUsername("julia");
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(artist.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        artistRepository.save(artist);

        assertThrows(ResourceNotFoundException.class, () -> {
            albumService.deleteAlbumByTitle("wrong title");});
    }
}
