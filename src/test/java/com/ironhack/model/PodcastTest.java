package com.ironhack.model;

import com.ironhack.security.utils.ArtistStatus;
import com.ironhack.security.model.Artist;
import com.ironhack.security.model.User;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PodcastTest {
    @Test
    public void podcastVoidConstructorTest(){
        Podcast podcast = new Podcast();
        assertNotNull(podcast);
    }

    @Test
    public void podcastConstructorTest(){
        Podcast podcast = new Podcast(4, 2, "comedy");
        assertNotNull(podcast);
        Podcast podcast1 = new Podcast("title", "3:42", null, 4, 2, "philosophy");
        assertNotNull(podcast1);
    }

    @Test
    public void podcastSetterGetterTest() {
        Artist artist = new Artist(new User(null, "artist", "ju", "1234",
                false, ArtistStatus.ACTIVE, new ArrayList<>(), null));

        Podcast podcast = new Podcast();
        podcast.setTitle("new title");
        podcast.setDuration("47:42");
        podcast.setArtist(artist);
        podcast.setSeason(4);
        podcast.setChapter(5);
        podcast.setCategory("comedy");

        assertEquals("new title", podcast.getTitle());
        assertEquals("47:42", podcast.getDuration());
        assertEquals("artist", podcast.getArtist().getName());
        assertEquals(4, podcast.getSeason());
        assertEquals(5, podcast.getChapter());
        assertEquals("comedy", podcast.getCategory());
    }
}
