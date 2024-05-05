package com.ironhack.repository;

import com.ironhack.demosecurityjwt.security.Utils.ArtistStatus;
import com.ironhack.demosecurityjwt.security.models.Artist;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.repositories.ArtistRepository;
import com.ironhack.model.Album;
import com.ironhack.model.Audio;
import com.ironhack.model.Playlist;
import com.ironhack.model.Song;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PlaylistRepositoryTest {
    @Autowired
    PlaylistRepository playlistRepository;

    @Autowired
    AudioRepository audioRepository;

    @Autowired
    ArtistRepository artistRepository;

    private Playlist playlist;

    @BeforeEach
    void setUp(){
        Artist artist = new Artist(new User(null, "artist", "artist", "1234",
                true, ArtistStatus.ACTIVE, new ArrayList<>(), null));
        Artist artistSaved = artistRepository.save(artist);

        Audio audio = new Audio("title2", "3:24", artistSaved);
        audioRepository.save(audio);
        List<Audio> audioList = new ArrayList<>();
        audioList.add(audio);

        playlist = Playlist.builder()
                .name("summer hits")
                .audios(audioList)
                .build();
        assertNotNull(playlist);

        playlistRepository.save(playlist);
    }

    @AfterEach
    void tearsDown(){
        playlistRepository.deleteAll();
        audioRepository.deleteAll();
        artistRepository.deleteAll();
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
        assertEquals(1, audioRepository.count());
        assertEquals(1, artistRepository.count());

        playlistRepository.delete(playlist);

        assertEquals(0, playlistRepository.count());
        assertEquals(1, audioRepository.count()); //audio is still present
        assertEquals(1, artistRepository.count()); //artist is still present
    }

    @Test
    void findByNameTest(){
        List<Playlist> playlistList = playlistRepository.findByName("summer hits");
        assertEquals(1, playlistList.size());
        assertEquals(playlistList.get(0).getName(), "summer hits");
    }

    @Test
    void findByNameNotExistingTest(){
        assertTrue(playlistRepository.findByName("wrong").isEmpty());
    }
}