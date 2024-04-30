package com.ironhack.demosecurityjwt.models;

import com.ironhack.demosecurityjwt.security.models.Artist;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.repositories.ArtistRepository;
import com.ironhack.model.Album;
import com.ironhack.model.Audio;
import com.ironhack.model.Song;
import com.ironhack.repository.AlbumRepository;
import com.ironhack.repository.SongRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
        User user = new User(null, "Coldplay", "co", "1234", false, new ArrayList<>(), null);
        Artist artist = new Artist(user);
        assertNotNull(artist);
    }

    @Test
    public void artistSetterGetterTest(){
        List<Audio> songs = new ArrayList<>();
        songs.add(new Song(null, "pop"));

        List<Album> albums = new ArrayList<>();
        albums.add(new Album());

        User user = new User(null, "Coldplay", "co", "1234", false, new ArrayList<>(), null);
        Artist artist = new Artist(user);

        artist.setName("julia");
        artist.setAlbums(albums);
        artist.setAudios(songs);

        assertEquals("julia", artist.getName());
        assertEquals(1, artist.getAlbums().size());
        assertEquals(1, artist.getAudios().size());
    }
}
