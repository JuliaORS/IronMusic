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
    private Artist artist;
    private Album album;

    @BeforeEach
    void setUp(){
        artist = new Artist(new User(null, "artist", "artist", "1234", true, new ArrayList<>(), null));
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
    public void saveAlbumCorrectInfoTest() throws Exception {
        Artist newArtist = new Artist();
        newArtist.setUsername("julia");
        artistRepository.save(newArtist);

        Song song =  new Song("title", "3:14", newArtist, null, "pop");
        List<Song> songList = new ArrayList<>();
        songList.add(song);
        songRepository.save(song);
        Album newAlbum =  new Album(" new album title", null, null);
        album.getSongs().add(song);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(newArtist.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        newAlbum.setArtist(newArtist);

        String expectedJson = objectMapper.writeValueAsString(new AlbumGeneralInfoDTO(newAlbum));
        AlbumGeneralInfoDTO result = albumService.saveAlbum(newAlbum);
        String resultJson = objectMapper.writeValueAsString(result);

        assertEquals(2, albumRepository.count());
        assertEquals(expectedJson, resultJson);
    }

    @Test
    public void saveAlbumEmptyTitleTest() throws Exception {
        Album newAlbum =  new Album("", null, null);
        Artist newArtist = new Artist();
        newArtist.setUsername("julia");
        artistRepository.save(newArtist);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(newArtist.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        assertThrows(ConstraintViolationException.class, () -> {albumService.saveAlbum(newAlbum);});
    }

    @Test
    public void deleteAlbumExistingTitleTest(){
        assertFalse(albumRepository.findByTitleAndArtistUsername(album.getTitle(), artist.getUsername()).isEmpty());
        assertEquals(2, songRepository.findByTitleContaining("title").size());

        albumService.deleteAlbumByTitle(album.getTitle());

        assertTrue(albumRepository.findByTitleAndArtistUsername(album.getTitle(), artist.getUsername()).isEmpty());
        assertEquals(0, songRepository.findByTitleContaining("title").size());
    }

    @Test
    public void deleteAlbumNotExistingTitleTest(){
        assertThrows(ResourceNotFoundException.class, () -> {
            albumService.deleteAlbumByTitle("wrong title");});
    }

    @Test
    public void addSongToAlbumTest(){
        Song song3 =  new Song("song title 3", "2:14", artist, null, "pop");
        songRepository.save(song3);
        Long id = song3.getId();
        assertEquals(3, songRepository.count());
        assertEquals(2, albumRepository.findByTitle("album title").get(0).getSongs().size());
        albumService.addSongToAlbumByTitleSong("album title", "song title 3");
        assertEquals(3, albumRepository.findByTitle("album title").get(0).getSongs().size());
        assertEquals("album title", songRepository.findById(id).get().getAlbum().getTitle());
    }

    @Test
    public void addSongToAlbumNotExistingAlbumTest(){
        assertThrows(ResourceNotFoundException.class, () -> {albumService.addSongToAlbumByTitleSong(
                "wrong album", "song title 1");});
    }

    @Test
    public void addSongToAlbumNotExistingSongTest(){
        assertThrows(ResourceNotFoundException.class, () -> {albumService.addSongToAlbumByTitleSong(
                "album title", "wrong title");});
    }

    @Test
    public void removeSongFromAlbumTest(){
        assertEquals(2, albumRepository.findByTitle("album title").get(0).getSongs().size());
        assertFalse(songRepository.findByTitleContaining("song title 1").isEmpty());
        albumService.removeSongFromAlbum("album title", "song title 1");
        assertEquals(1, albumRepository.findByTitle("album title").get(0).getSongs().size());
        assertFalse(songRepository.findByTitleContaining("song title 1").isEmpty()); //song still exists
    }

    @Test
    public void removeSongFromAlbumNotExistingAlbumTest(){
        assertThrows(ResourceNotFoundException.class, () -> {albumService.removeSongFromAlbum(
                "wrong album", "song title 1");});
    }

    @Test
    public void removeSongFromAlbumNotExistingSongTest(){
        assertThrows(ResourceNotFoundException.class, () -> {albumService.removeSongFromAlbum(
                "album title", "wrong song");});
    }
}
