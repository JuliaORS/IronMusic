package com.ironhack.security.models;

import com.ironhack.security.utils.ArtistStatus;
import com.ironhack.security.model.Artist;
import com.ironhack.security.model.User;
import com.ironhack.model.Album;
import com.ironhack.model.Audio;
import com.ironhack.model.Song;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ArtistTest {
    @Test
    public void artistConstructorVoidTest(){
        Artist artist = new Artist();
        assertNotNull(artist);
    }

    @Test
    public void artistConstructorByUserTest(){
        User user = new User(null, "artist", "artist", "1234",
                true, ArtistStatus.ACTIVE, new ArrayList<>(), null);
        Artist artist = new Artist(user);
        assertNotNull(artist);
    }

    @Test
    public void artistSetterGetterTest(){
        List<Audio> songs = new ArrayList<>();
        songs.add(new Song(null, "pop"));

        List<Album> albums = new ArrayList<>();
        albums.add(new Album());

        User user = new User(null, "artist", "artist", "1234",
                true, ArtistStatus.ACTIVE, new ArrayList<>(), null);
        Artist artist = new Artist(user);

        artist.setName("julia");
        artist.setAlbums(albums);
        artist.setAudios(songs);

        assertEquals("julia", artist.getName());
        assertEquals(1, artist.getAlbums().size());
        assertEquals(1, artist.getAudios().size());
    }
}
