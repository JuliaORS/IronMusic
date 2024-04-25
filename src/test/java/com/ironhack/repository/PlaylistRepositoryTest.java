package com.ironhack.repository;

import com.ironhack.model.Playlist;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class PlaylistRepositoryTest {
    @Autowired
    PlaylistRepository playlistRepository;
    private Playlist playlist;

    @BeforeEach
    void setUp(){
        playlist = Playlist.builder()
                .name("summer hits")
                .audios(null)
                .build();
        assertNotNull(playlist);
        playlistRepository.save(playlist);
    }

    @AfterEach
    void tearsDown(){
        playlistRepository.deleteAll();
    }

    @Test
    void savePlaylistTest(){
        assertEquals(1, playlistRepository.count());
        Playlist newPlaylist = Playlist.builder()
                .name("spring hits")
                .audios(null)
                .build();
        Playlist playlistSaved = playlistRepository.save(newPlaylist);
        assertEquals(playlistSaved.getName(), newPlaylist.getName());
        assertEquals(2, playlistRepository.count());
    }

    @Test
    void deletePlaylistTest(){
        assertEquals(1, playlistRepository.count());
        playlistRepository.delete(playlist);
        assertEquals(0, playlistRepository.count());
    }
}