package com.ironhack.model;

import com.ironhack.demosecurityjwt.security.models.Artist;
import com.ironhack.demosecurityjwt.security.models.User;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SongTest {
    @Test
    public void songVoidConstructorTest(){
        Song song = new Song();
        assertNotNull(song);
    }

    @Test
    public void songConstructorTest(){
        Song song = new Song(null, "pop");
        assertNotNull(song);
        Song song1 = new Song("title", "3:42", null, null, "pop");
        assertNotNull(song1);
    }

    @Test
    public void songSetterGetterTest() {
        Artist artist = new Artist(new User(null, "artist", "ju", "1234", new ArrayList<>(), null));
        Album album = new Album("album title", null, null);

        Song song = new Song();
        song.setTitle("Yellow");
        song.setDuration("3:42");
        song.setArtist(artist);
        song.setAlbum(album);
        song.setGenre("rock");

        assertEquals("Yellow", song.getTitle());
        assertEquals("3:42", song.getDuration());
        assertEquals("artist", song.getArtist().getName());
        assertEquals("album title", song.getAlbum().getTitle());
        assertEquals("rock", song.getGenre());
    }
}
