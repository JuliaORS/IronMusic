package com.ironhack.security.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.model.Audio;
import com.ironhack.model.Playlist;
import com.ironhack.repository.AudioRepository;
import com.ironhack.security.dto.UserGeneralInfoDTO;
import com.ironhack.security.model.Artist;
import com.ironhack.security.model.Role;
import com.ironhack.security.model.User;
import com.ironhack.security.repository.ArtistRepository;
import com.ironhack.security.repository.RoleRepository;
import com.ironhack.security.repository.UserRepository;
import com.ironhack.security.service.impl.ArtistService;
import com.ironhack.security.utils.ArtistStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ArtistServiceTest {
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private AudioRepository audioRepository;
    @Autowired
    private ArtistService artistService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Artist artist;

    @BeforeEach
    void setUp(){
        artist = new Artist(new User(null, "artist", "artist", "1234",
                true, ArtistStatus.ACTIVE,  new ArrayList<>(), null));
        artistRepository.save(artist);

        Audio audio1 = new Audio("audio title 1", "3:34", artist);
        audioRepository.save(audio1);
        Audio audio2 = new Audio("audio title 2", "3:40", artist);
        audioRepository.save(audio2);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(artist.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    void tearsDown(){
        artistRepository.deleteAll();
        audioRepository.deleteAll();
    }

    @Test
    public void getOwnAudiosTest() throws Exception{
        List<Audio> audioList = audioRepository.findByArtistUsername("artist");
        List<AudioGeneralInfoDTO> audioGeneralInfoDTOS = new ArrayList<>();
        for(Audio audio : audioList){
            audioGeneralInfoDTOS.add(new AudioGeneralInfoDTO(audio));
        }
        String expectedJson = objectMapper.writeValueAsString(audioGeneralInfoDTOS);
        String resultJson = objectMapper.writeValueAsString(artistService.getOwnAudios());
        assertEquals(expectedJson, resultJson);
    }
}
