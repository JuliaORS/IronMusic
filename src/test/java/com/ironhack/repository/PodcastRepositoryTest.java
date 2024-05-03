package com.ironhack.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.demosecurityjwt.security.models.Artist;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.repositories.ArtistRepository;
import com.ironhack.model.Podcast;
import com.ironhack.model.Podcast;
import com.ironhack.model.Song;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PodcastRepositoryTest {
    @Autowired
    PodcastRepository podcastRepository;
    @Autowired
    ArtistRepository artistRepository;
    private Podcast podcast;
    @BeforeEach
    void setUp(){
        Artist artist = new Artist(new User(null, "Coldplay", "co", "1234", false, false, new ArrayList<>(), null));
        Artist artistSaved = artistRepository.save(artist);
        podcast = new Podcast("new title", "45:42", artistSaved, 4, 2, "philosophy");
        Podcast podcast2 = new Podcast("title", "45:42", null, 4, 2, "philosophy");
        podcastRepository.save(podcast);
        podcastRepository.save(podcast2);
    }

    @AfterEach
    void tearsDown(){
        podcastRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    void savePodcastTest(){
        assertEquals(2, podcastRepository.count());
        Podcast newPodcast =  new Podcast("new title", "35:13", null, 5, 1, "comedy");
        Podcast podcastSaved = podcastRepository.save(newPodcast);
        assertEquals(podcastSaved.getTitle(), newPodcast.getTitle());
        assertEquals(3, podcastRepository.count());
    }
    @Test
    void deletePodcastTest() throws Exception{
        assertEquals(2, podcastRepository.count());

        podcastRepository.delete(podcast);

        assertEquals(1, podcastRepository.count());
    }
    @Test
    void findByTitleContainingTest(){
        List<Podcast> podcasts = podcastRepository.findByTitleContaining("new");
        assertEquals(1, podcasts.size());
        assertEquals(podcast.getTitle(), podcasts.get(0).getTitle());
    }

    @Test
    void findByTitleContainingAnyPodcastTest(){
        assertEquals(0, podcastRepository.findByTitleContaining("to").size());
    }

    @Test
    void findByArtistNameContainingTest(){
        List<Podcast> podcasts = podcastRepository.findByArtistNameContaining("Co");
        assertEquals(1, podcasts.size());
        assertEquals(podcast.getTitle(), podcasts.get(0).getTitle());
    }

    @Test
    void findByArtistNameContainingAnyPodcastTest(){
        assertEquals(0, podcastRepository.findByTitleContaining("to").size());
    }

}
