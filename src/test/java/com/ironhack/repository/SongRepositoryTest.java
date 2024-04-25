package com.ironhack.repository;

import com.ironhack.model.Song;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SongRepositoryTest {

    @Autowired
    SongRepository songRepository;
    private Song song;

    @BeforeEach
    void setUp(){
        song = new Song("title", "5:34", null, null, "pop");
        songRepository.save(song);
    }

    @AfterEach
    void tearsDown(){
        songRepository.deleteAll();
    }

    @Test
    void saveSongTest(){
        assertEquals(1, songRepository.count());
        Song newSong =  new Song("new title", "5:13", null, null, "rock");
        Song songSaved = songRepository.save(newSong);
        assertEquals(songSaved.getTitle(), newSong.getTitle());
        assertEquals(2, songRepository.count());
    }
    @Test
    void deleteSongTest(){
        assertEquals(1, songRepository.count());
        songRepository.delete(song);
        assertEquals(0, songRepository.count());
    }
}
