package com.ironhack.repository;

import com.ironhack.model.Album;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AlbumRepositoryTest {
    @Autowired
    AlbumRepository albumRepository;
    private Album album;
    @BeforeEach
    void setUp(){
        album = new Album("title", null, null);
        albumRepository.save(album);
    }

    @AfterEach
    void tearsDown(){
        albumRepository.deleteAll();
    }

    @Test
    void saveAlbumTest(){
        assertEquals(1, albumRepository.count());
        Album newAlbum = new Album("new title", null, null);
        Album albumSaved = albumRepository.save(newAlbum);
        assertEquals(albumSaved.getTitle(), newAlbum.getTitle());
        assertEquals(2, albumRepository.count());
    }

    @Test
    void deleteAlbumTest(){
        assertEquals(1, albumRepository.count());
        albumRepository.delete(album);
        assertEquals(0, albumRepository.count());
    }
}
