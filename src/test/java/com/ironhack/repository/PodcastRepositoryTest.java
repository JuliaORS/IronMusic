package com.ironhack.repository;

import com.ironhack.model.Podcast;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PodcastRepositoryTest {
    @Autowired
    PodcastRepository podcastRepository;
    private Podcast podcast;

    @BeforeEach
    void setUp(){
        podcast = new Podcast("title", "45:42", null, 4, 2, "philosophy");
        podcastRepository.save(podcast);
    }

    @AfterEach
    void tearsDown(){
        podcastRepository.deleteAll();
    }

    @Test
    void savePodcastTest(){
        assertEquals(1, podcastRepository.count());
        Podcast newPodcast =  new Podcast("new title", "35:13", null, 5, 1, "comedy");
        Podcast podcastSaved = podcastRepository.save(newPodcast);
        assertEquals(podcastSaved.getTitle(), newPodcast.getTitle());
        assertEquals(2, podcastRepository.count());
    }
    @Test
    void deletePodcastTest(){
        assertEquals(1, podcastRepository.count());
        podcastRepository.delete(podcast);
        assertEquals(0, podcastRepository.count());
    }
}
