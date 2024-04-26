package com.ironhack.repository;

import com.ironhack.demosecurityjwt.security.models.Artist;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.repositories.ArtistRepository;
import com.ironhack.model.Song;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SongRepositoryTest {

    @Autowired
    SongRepository songRepository;
    @Autowired
    ArtistRepository artistRepository;
    private Song song;

    @BeforeEach
    void setUp(){
        Artist artist = new Artist(new User(null, "Coldplay", "co", "1234", new ArrayList<>(), null));
        Artist artistSaved = artistRepository.save(artist);
        song = new Song("title", "5:34", artistSaved, null, "pop");
        songRepository.save(song);
    }

    @AfterEach
    void tearsDown(){
        songRepository.deleteAll();
        artistRepository.deleteAll();
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
        List<Song> songs = songRepository.findByTitleContaining("title");

        Song songToDelete = songs.get(0);
        songRepository.delete(songs.get(0));
        assertEquals(0, songRepository.count());
    }

    @Test
    void findByTitleContainingTest(){
        List<Song> songs = songRepository.findByTitleContaining("ti");
        assertEquals(1, songs.size());
        assertEquals(song.getTitle(), songs.get(0).getTitle());
    }

    @Test
    void findByTitleContainingAnySongTest(){
        assertEquals(0, songRepository.findByTitleContaining("to").size());
    }

    @Test
    void findByArtistNameContainingTest(){
        List<Song> songs = songRepository.findByArtistNameContaining("Co");
        assertEquals(1, songs.size());
        assertEquals(song.getTitle(), songs.get(0).getTitle());
    }

    @Test
    void findByArtistNameContainingAnySongTest(){
        assertEquals(0, songRepository.findByTitleContaining("to").size());
    }

    @Test
    void findByGenreContainingTest(){
        List<Song> songs = songRepository.findByGenreContaining("pop");
        assertEquals(1, songs.size());
        assertEquals(song.getTitle(), songs.get(0).getTitle());
    }

    @Test
    void findByGenreContainingAnySongTest(){
        assertEquals(0, songRepository.findByGenreContaining("rock").size());
    }
}
