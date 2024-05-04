package com.ironhack.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.demosecurityjwt.security.Utils.ArtistStatus;
import com.ironhack.demosecurityjwt.security.models.Artist;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.repositories.ArtistRepository;
import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.exceptions.BadRequestFormatException;
import com.ironhack.exceptions.ResourceNotFoundException;
import com.ironhack.model.Audio;
import com.ironhack.model.Audio;
import com.ironhack.repository.AudioRepository;
import com.ironhack.service.impl.AudioService;
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
public class AudioServiceTest {
    @Autowired
    private AudioService audioService;
    @Autowired
    private AudioRepository audioRepository;
    @Autowired
    private ArtistRepository artistRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Artist artist;
    private Audio audio;

    @BeforeEach
    void setUp(){
        artist = new Artist(new User(null, "artist", "julia", "1234",
                true, ArtistStatus.ACTIVE, new ArrayList<>(), null));
        artistRepository.save(artist);

        audio =  new Audio("audio title", "2:15", artist);
        audioRepository.save(audio);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(artist.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    void tearsDown(){
        audioRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    public void deleteAudioExistingTitleTest(){
        assertFalse(audioRepository.findByTitle("audio title").isEmpty());
        audioService.deleteAudioByTitle("audio title");
        assertTrue(audioRepository.findByTitle("audio title").isEmpty());
    }

    @Test
    public void deleteAudioNotExistingIdTest(){
        assertThrows(ResourceNotFoundException.class, () -> {
            audioService.deleteAudioByTitle("wrong title");});
    }

    @Test
    public void getAudioByAllInfoExistingInfoTest() throws Exception{
        List<Audio> audios = new ArrayList<>();
        audios.add(audio);
        List<AudioGeneralInfoDTO> audioGeneralInfoDTOS = new ArrayList<>();
        for(Audio audio : audios){
            audioGeneralInfoDTOS.add(new AudioGeneralInfoDTO(audio));
        }
        String expectedJson = objectMapper.writeValueAsString(audioGeneralInfoDTOS);
        String resultJson = objectMapper.writeValueAsString(audioService.getAudioByAllInfo("title"));
        assertEquals(expectedJson, resultJson);
    }

    @Test
    public void getAudioByAllInfoNotExistingInfoTest() throws Exception{
        assertThrows(ResourceNotFoundException.class, () -> {
            audioService.getAudioByAllInfo("wrong");});
    }

}
