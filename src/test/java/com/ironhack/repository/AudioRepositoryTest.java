package com.ironhack.repository;

import com.ironhack.model.Audio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class AudioRepositoryTest {
    @Autowired
    AudioRepository audioRepository;
    private Audio audio;
    @BeforeEach
    void setUp(){
        audio = Audio.audioBuilder()
                .title("Title")
                .duration("3:23")
                .artist(null)
                .build();
        assertNotNull(audio);
        audioRepository.save(audio);
    }

    @AfterEach
    void tearsDown(){
        audioRepository.deleteAll();
    }

    @Test
    void saveAudioTest(){
        assertEquals(1, audioRepository.count());
        Audio newAudio = Audio.audioBuilder()
                .title("new title")
                .duration("4:25")
                .artist(null)
                .build();
        Audio audioSaved = audioRepository.save(newAudio);
        assertEquals(audioSaved.getTitle(), newAudio.getTitle());
        assertEquals(2, audioRepository.count());
    }

    @Test
    void deleteAudioTest(){
        assertEquals(1, audioRepository.count());
        audioRepository.delete(audio);
        assertEquals(0, audioRepository.count());
    }
}
