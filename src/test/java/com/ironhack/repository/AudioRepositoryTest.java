package com.ironhack.repository;

import com.ironhack.demosecurityjwt.security.Utils.ArtistStatus;
import com.ironhack.demosecurityjwt.security.models.Artist;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.repositories.ArtistRepository;
import com.ironhack.model.Audio;
import com.ironhack.model.Song;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AudioRepositoryTest {
    @Autowired
    AudioRepository audioRepository;
    @Autowired
    ArtistRepository artistRepository;
    private Audio audio;
    private Audio audio1;
    @BeforeEach
    void setUp(){
        Artist artist = new Artist(new User(null, "artist", "artist", "1234",
                false, ArtistStatus.ACTIVE, new ArrayList<>(), null));
        Artist artistSaved = artistRepository.save(artist);

        audio = new Audio("YELLOW111", "3:34", artistSaved);
        audio1 = new Audio("YELLOW", "3:34", artistSaved);
        audioRepository.save(audio);
        audioRepository.save(audio1);
    }

    @AfterEach
    void tearsDown(){
        audioRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    void saveAudioTest(){
        assertEquals(2, audioRepository.count());
        Artist artist = new Artist(new User(null, "artist2", "artist2", "1234",
                true, ArtistStatus.ACTIVE,  new ArrayList<>(), null));
        Artist artistSaved = artistRepository.save(artist);
        Audio newAudio = Audio.audioBuilder()
                .title("new title")
                .duration("4:25")
                .artist(artistSaved)
                .build();
        Audio audioSaved = audioRepository.save(newAudio);
        assertEquals(audioSaved.getTitle(), newAudio.getTitle());
        assertEquals(3, audioRepository.count());
    }

    @Test
    void deleteAudioTest(){
        assertEquals(2, audioRepository.count());
        audioRepository.delete(audio);
        assertEquals(1, audioRepository.count());
        assertTrue(artistRepository.findByUsername("artist").isPresent());
    }

    @Test
    void findByTitleContainingTest(){
        List<Audio> audios = audioRepository.findByTitleContaining("111");
        assertEquals(1, audios.size());
        assertEquals(audio.getTitle(), audios.get(0).getTitle());
    }
}



