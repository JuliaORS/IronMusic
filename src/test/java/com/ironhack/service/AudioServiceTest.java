package com.ironhack.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.demosecurityjwt.security.models.Artist;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.repositories.ArtistRepository;
import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.exceptions.BadRequestFormatException;
import com.ironhack.exceptions.ResourceNotFoundException;
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
    private Audio audio;

    @BeforeEach
    void setUp(){
        Artist artist = new Artist();
        artist.setName("julia");
        artistRepository.save(artist);

        audio =  new Audio("title", "2:15", artist);
        audioRepository.save(audio);
    }

    @AfterEach
    void tearsDown(){
        audioRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    public void deleteAudioExistingIdTest(){
        Long id = audio.getId();

        assertTrue(audioRepository.findById(id).isPresent());
        audioService.deleteAudio(id);
        assertFalse(audioRepository.findById(id).isPresent());
    }

    @Test
    public void deleteAudioNotExistingIdTest(){
        assertThrows(ResourceNotFoundException.class, () -> {
            audioService.deleteAudio(45L);});
    }

}
