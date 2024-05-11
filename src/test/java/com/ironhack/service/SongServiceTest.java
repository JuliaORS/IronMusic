package com.ironhack.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.dto.SongGeneralInfoDTO;
import com.ironhack.security.utils.ArtistStatus;
import com.ironhack.security.model.Artist;
import com.ironhack.security.model.Role;
import com.ironhack.security.model.User;
import com.ironhack.security.repository.ArtistRepository;
import com.ironhack.security.repository.RoleRepository;
import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.exception.BadRequestFormatException;
import com.ironhack.exception.ResourceNotFoundException;
import com.ironhack.model.Song;
import com.ironhack.repository.SongRepository;
import com.ironhack.service.impl.SongService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SongServiceTest {
    @Autowired
    private SongService songService;
    @Autowired
    private SongRepository songRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private RoleRepository roleRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<Song> songsList = new ArrayList<>();
    private Song song1;
    private Song song2;
    private Artist artist;

    @BeforeEach
    void setUp(){
        artist = new Artist(new User(null, "artist", "artist", "1234",
                true, ArtistStatus.ACTIVE, new ArrayList<>(), null));
        artistRepository.save(artist);
        song1 =  new Song("song title 1", "5:13", artist, null, "rock");
        song2 =  new Song("song title 2", "3:14", artist, null, "pop");
        songsList.add(song1);
        songsList.add(song2);

        songRepository.saveAll(songsList);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(artist.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    void tearsDown(){
        songRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    public void saveSongCorrectInfoTest() throws Exception {
        Song song =  new Song("song title 3", "2:15", null, null, "funk");
        Artist newArtist = new Artist(new User(null, "artist1", "artist_1", "1234",
                true, ArtistStatus.ACTIVE, new ArrayList<>(), null));
        Collection<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("ROLE_USER").get());
        roles.add(roleRepository.findByName("ROLE_ARTIST").get());
        newArtist.setRoles(roles);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(newArtist.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        artistRepository.save(newArtist);
        song.setArtist(newArtist);

        String expectedJson = objectMapper.writeValueAsString(new SongGeneralInfoDTO(song));
        SongGeneralInfoDTO result = songService.saveSong(song);
        String resultJson = objectMapper.writeValueAsString(result);

        assertEquals(3, songRepository.count());
        assertEquals(expectedJson, resultJson);
    }

    @Test
    public void saveSongWrongDurationFormatTest() throws Exception {
        Song song =  new Song("title", "2a15", null, null, "funk");
        Artist newArtist = new Artist(new User(null, "artist1", "artist_1", "1234",
                true, ArtistStatus.ACTIVE, new ArrayList<>(), null));
        Collection<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("ROLE_USER").get());
        roles.add(roleRepository.findByName("ROLE_ARTIST").get());
        newArtist.setRoles(roles);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(newArtist.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        artistRepository.save(newArtist);
        song.setArtist(newArtist);

        assertThrows(BadRequestFormatException.class, () -> {songService.saveSong(song);});
    }

    @Test
    public void saveSongEmptyTitleTest() throws Exception {
        Song Song =  new Song("", "2:15", null,  null,"pop");
        Artist newArtist = new Artist(new User(null, "singer1", "ju", "1234",
                true, ArtistStatus.ACTIVE, new ArrayList<>(), null));
        Collection<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("ROLE_USER").get());
        roles.add(roleRepository.findByName("ROLE_ARTIST").get());
        newArtist.setRoles(roles);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(newArtist.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        artistRepository.save(newArtist);
        Song.setArtist(newArtist);

        assertThrows(ConstraintViolationException.class, () -> {songService.saveSong(Song);});
    }

    @Test
    public void deleteSongExistingTitleTest(){
        assertFalse(songRepository.findByTitle("song title 2").isEmpty());
        songService.deleteSongByTitle("song title 2");
        assertTrue(songRepository.findByTitle("song title 2").isEmpty());
    }

    @Test
    public void deleteSongNotExistingIdTest(){
        assertThrows(ResourceNotFoundException.class, () -> {
            songService.deleteSongByTitle("wrong title");});
    }

    @Test
    public void getAllSongsTest() throws Exception{
        List<SongGeneralInfoDTO> audioGeneralInfoDTOS = new ArrayList<>();
        for(Song song : songsList){
            audioGeneralInfoDTOS.add(new SongGeneralInfoDTO(song));
        }
        String expectedJson = objectMapper.writeValueAsString(audioGeneralInfoDTOS);
        String resultJson = objectMapper.writeValueAsString(songService.getAllSongs());
        assertEquals(expectedJson, resultJson);
    }

    @Test
    public void getSongByTitleExistingSongTest() throws Exception{
        List<Song> songs = new ArrayList<>();
        songs.add(song2);
        List<SongGeneralInfoDTO> audioGeneralInfoDTOS = new ArrayList<>();
        for(Song song : songs){
            audioGeneralInfoDTOS.add(new SongGeneralInfoDTO(song));
        }
        String expectedJson = objectMapper.writeValueAsString(audioGeneralInfoDTOS);
        String resultJson = objectMapper.writeValueAsString(songService.getSongByTitle("2"));
        assertEquals(expectedJson, resultJson);
    }

    @Test
    public void getSongByTitleNotExistingSongTest() throws Exception{
        assertThrows(ResourceNotFoundException.class, () -> {
            songService.getSongByTitle("wrong title");});
    }

    @Test
    public void getSongByArtistNameExistingSongTest() throws Exception{
        List<Song> songs = new ArrayList<>();
        songs.add(song1);
        songs.add(song2);
        List<SongGeneralInfoDTO> audioGeneralInfoDTOS = new ArrayList<>();
        for(Song song : songs){
            audioGeneralInfoDTOS.add(new SongGeneralInfoDTO(song));
        }
        String expectedJson = objectMapper.writeValueAsString(audioGeneralInfoDTOS);
        String resultJson = objectMapper.writeValueAsString(songService.getSongByArtistName("artist"));
        assertEquals(expectedJson, resultJson);
    }

    @Test
    public void getSongByArtistNameNotExistingSongTest() throws Exception{
        assertThrows(ResourceNotFoundException.class, () -> {
            songService.getSongByArtistName("wrong artist");});
    }

    @Test
    public void getSongByGenreExistingSongTest() throws Exception{
        List<Song> songs = new ArrayList<>();
        songs.add(song2);
        List<SongGeneralInfoDTO> audioGeneralInfoDTOS = new ArrayList<>();
        for(Song song : songs){
            audioGeneralInfoDTOS.add(new SongGeneralInfoDTO(song));
        }
        String expectedJson = objectMapper.writeValueAsString(audioGeneralInfoDTOS);
        String resultJson = objectMapper.writeValueAsString(songService.getSongByGenre("pop"));
        assertEquals(expectedJson, resultJson);
    }

    @Test
    public void getSongByGenreNotExistingSongTest() throws Exception{
        assertThrows(ResourceNotFoundException.class, () -> {
            songService.getSongByGenre("wrong genre");});
    }

    @Test
    public void getSongByAllInfoExistingInfoTest() throws Exception{
        List<Song> songs = new ArrayList<>();
        songs.add(song2);
        List<SongGeneralInfoDTO> audioGeneralInfoDTOS = new ArrayList<>();
        for(Song song : songs){
            audioGeneralInfoDTOS.add(new SongGeneralInfoDTO(song));
        }
        String expectedJson = objectMapper.writeValueAsString(audioGeneralInfoDTOS);
        String resultJson = objectMapper.writeValueAsString(songService.getSongByAllInfo("2"));
        assertEquals(expectedJson, resultJson);
    }

    @Test
    public void getSongByAllInfoNotExistingInfoTest() throws Exception{
        assertThrows(ResourceNotFoundException.class, () -> {
            songService.getSongByAllInfo("wrong");});
    }
}
