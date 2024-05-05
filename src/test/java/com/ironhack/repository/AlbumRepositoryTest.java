package com.ironhack.repository;

import com.ironhack.security.utils.ArtistStatus;
import com.ironhack.security.model.Artist;
import com.ironhack.security.model.User;
import com.ironhack.security.repository.ArtistRepository;
import com.ironhack.model.Album;
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
public class AlbumRepositoryTest {
    @Autowired
    AlbumRepository albumRepository;
    @Autowired
    ArtistRepository artistRepository;
    @Autowired
    SongRepository songRepository;

    @BeforeEach
    void setUp(){
        Artist artist = new Artist(new User(null, "artist", "artist", "1234",
                false, ArtistStatus.ACTIVE,  new ArrayList<>(), null));
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
        assertTrue(artistRepository.findByUsername("artist").isPresent());

        Album albumToDelete = albumRepository.findByTitle("album").get(0);
        albumRepository.delete(albumToDelete);

        assertEquals(0, albumRepository.count());
        assertTrue(songRepository.findByTitleContaining("title1").isEmpty()); //check delete song
        assertTrue(artistRepository.findByUsername("artist").isPresent()); //check artist is still present
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
        List<Album> albumList = albumRepository.findByTitleAndArtistUsername("album", "artist");
        assertEquals(1, albumList.size());
        assertEquals("album", albumList.get(0).getTitle());
    }

    @Test
    void findByTitleAndArtistUsernameWrongTitleTest(){
        List<Album> albumList = albumRepository.findByTitleAndArtistUsername("wrong", "ju");
        assertEquals(0, albumList.size());
    }

    @Test
    void findByTitleAndArtistUsernameWrongUsernameTest(){
        List<Album> albumList = albumRepository.findByTitleAndArtistUsername("album", "wrong");
        assertEquals(0, albumList.size());
    }

    @Test
    void findByTitleContainingTest(){
        List<Album> albums = albumRepository.findByTitleContaining("al");
        assertEquals(1, albums.size());
        assertEquals("album", albums.get(0).getTitle());
    }

    @Test
    void findByTitleContainingAnyAlbumTest(){
        assertEquals(0, albumRepository.findByTitleContaining("wrong").size());
    }

    @Test
    void findByArtistNameContainingTest(){
        List<Album> albums = albumRepository.findByArtistNameContaining("artist");
        assertEquals(1, albums.size());
        assertEquals("album", albums.get(0).getTitle());
    }

    @Test
    void findByArtistNameContainingAnyAlbumTest(){
        assertEquals(0, albumRepository.findByTitleContaining("wrong").size());
    }

    @Test
    void findByArtistNameContainingOrTitleContainingTest(){
        List<Album> Albums = albumRepository.findByArtistNameContaining("art");
        assertEquals(1, Albums.size());
        assertEquals("album", Albums.get(0).getTitle());
    }

    @Test
    void findByArtistNameContainingOrTitleContainingNotExistingTest(){
        assertEquals(0, albumRepository.findByTitleContaining("wrong").size());
    }

}
