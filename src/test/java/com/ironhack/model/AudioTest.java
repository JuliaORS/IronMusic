package com.ironhack.model;

import com.ironhack.demosecurityjwt.security.models.Artist;
import com.ironhack.demosecurityjwt.security.models.User;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AudioTest {
    @Test
    public void audioVoidConstructorTest(){
        Audio audio = new Audio();
        assertNotNull(audio);
    }

    @Test
    public void audioConstructorTest(){
        Audio audio = Audio.audioBuilder()
                .title("Title")
                .duration("3:23")
                .artist(null)
                .build();
        assertNotNull(audio);
    }

    @Test
    public void audioSetterGetterTest() {
        Artist artist = new Artist(new User(null, "artist", "ju", "1234", new ArrayList<>(), null));

        Audio audio = new Audio();
        audio.setTitle("title");
        audio.setDuration("2:23");
        audio.setArtist(artist);

        assertEquals("title", audio.getTitle());
        assertEquals("2:23", audio.getDuration());
        assertEquals("artist", audio.getArtist().getName());
    }
}
