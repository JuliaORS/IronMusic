package com.ironhack.repository;

import com.ironhack.demosecurityjwt.security.models.Artist;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.repositories.ArtistRepository;
import com.ironhack.model.Album;
import com.ironhack.model.Song;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SongRepositoryTest {

    @Autowired
    SongRepository songRepository;
    @Autowired
    ArtistRepository artistRepository;
    @Autowired
    AlbumRepository albumRepository;
    private Song song;

    @BeforeEach
    void setUp(){
        Artist artist = new Artist(new User(null, "artist", "ju", "1234", new ArrayList<>(), null));
        Artist artistSaved = artistRepository.save(artist);

        song = new Song("title1", "3:34", artistSaved, null, "pop");
        songRepository.save(song);

        Album album = new Album("album", artistSaved, null);
        albumRepository.save(album);

        song.setAlbum(album);
        songRepository.save(song);

        List<Album> albumList = new ArrayList<>();
        albumList.add(album);
        artistSaved.setAlbums(albumList);
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
        assertFalse(songRepository.findByTitleContaining("title1").isEmpty());
        assertEquals(1, albumRepository.count());
        assertTrue(artistRepository.findByUsername("ju").isPresent());

        List<Song> songs = songRepository.findByTitleContaining("title");
        songRepository.delete(songs.get(0));

        assertTrue(songRepository.findByTitleContaining("title1").isEmpty());
        assertEquals(1, albumRepository.count());
        assertTrue(artistRepository.findByUsername("ju").isPresent());

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
        List<Song> songs = songRepository.findByArtistNameContaining("art");
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
