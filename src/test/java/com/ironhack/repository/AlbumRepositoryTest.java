package com.ironhack.repository;

import com.ironhack.demosecurityjwt.security.models.Artist;
import com.ironhack.demosecurityjwt.security.models.Role;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.repositories.ArtistRepository;
import com.ironhack.demosecurityjwt.security.repositories.RoleRepository;
import com.ironhack.model.Album;
import com.ironhack.model.Audio;
import com.ironhack.model.Song;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AlbumRepositoryTest {
    @Autowired
    AlbumRepository albumRepository;
    @Autowired
    ArtistRepository artistRepository;
    @Autowired
    SongRepository songRepository;

    @BeforeEach
    void setUp(){
        Artist artist = new Artist(new User(null, "artist", "ju", "1234", false, new ArrayList<>(), null));
        Artist artistSaved = artistRepository.save(artist);

        Song song = new Song("title1", "3:34", artistSaved, null, "pop");
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
       albumRepository.deleteAll();
       artistRepository.deleteAll();
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
        assertFalse(songRepository.findByTitleContaining("title1").isEmpty());
        assertEquals(1, albumRepository.count());
        assertTrue(artistRepository.findByUsername("ju").isPresent());

        Album albumToDelete = albumRepository.findByTitle("album").get(0);
        albumRepository.delete(albumToDelete);

        assertEquals(0, albumRepository.count());
        assertTrue(songRepository.findByTitleContaining("title1").isEmpty()); //check delete song
        assertTrue(artistRepository.findByUsername("ju").isPresent()); //check artist is still present
    }

    @Test
    void findByTitleTest(){
        List<Album> albumList = albumRepository.findByTitle("album");
        assertEquals(1, albumList.size());
        assertEquals("album", albumList.get(0).getTitle());
    }

    @Test
    void findByTitleNotExistingTest(){
        assertEquals(0, albumRepository.findByTitle("albu").size());
    }

    @Test
    void findByTitleAndArtistUsernameTest(){
        List<Album> albumList = albumRepository.findByTitleAndArtistUsername("album", "ju");
        assertEquals(1, albumList.size());
        assertEquals("album", albumList.get(0).getTitle());
    }

    @Test
    void findByTitleAndArtistUsernameWrongTitleTest(){
        List<Album> albumList = albumRepository.findByTitleAndArtistUsername("albu", "ju");
        assertEquals(0, albumList.size());
    }

    @Test
    void findByTitleAndArtistUsernameWrongUsernameTest(){
        List<Album> albumList = albumRepository.findByTitleAndArtistUsername("album", "julia");
        assertEquals(0, albumList.size());
    }
}
