package com.ironhack.model;

import com.ironhack.security.utils.ArtistStatus;
import com.ironhack.security.model.Artist;
import com.ironhack.security.model.User;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class AlbumTest {

    @Test
    public void albumVoidConstructorTest(){
        Album album = new Album();
        assertNotNull(album);
    }

    @Test
    public void albumConstructorTest(){
        Album album = new Album("title", null, null);
        assertNotNull(album);
    }

    @Test
    public void albumSetterGetterTest() {
        Artist artist = new Artist(new User(null, "artist", "ju", "1234",
                false, ArtistStatus.ACTIVE, new ArrayList<>(), null));
        List<Song> songs = new ArrayList<>();
        songs.add(new Song(null, "pop"));

        Album album = new Album();
        album.setTitle("title");
        album.setArtist(artist);
        album.setSongs(songs);

        assertEquals("title", album.getTitle());
        assertEquals("artist", album.getArtist().getName());
        assertEquals("pop", album.getSongs().get(0).getGenre());
    }
}


