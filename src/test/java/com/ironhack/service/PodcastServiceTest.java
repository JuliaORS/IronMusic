package com.ironhack.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.demosecurityjwt.security.models.Artist;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.repositories.ArtistRepository;
import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.exceptions.BadRequestFormatException;
import com.ironhack.exceptions.ResourceNotFoundException;
import com.ironhack.model.Podcast;
import com.ironhack.model.Song;
import com.ironhack.repository.PodcastRepository;
import com.ironhack.service.impl.PodcastService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PodcastServiceTest {

    @Autowired
    private PodcastService podcastService;
    @Autowired
    private PodcastRepository podcastRepository;
    @Autowired
    private ArtistRepository artistRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<Podcast> podcastsList = new ArrayList<>();
    private Podcast podcast1;
    private Podcast podcast2;

    @BeforeEach
    void setUp(){
        Artist artist = new Artist(new User(null, "podcaster", "co", "1234", true, new ArrayList<>(), null));
        artistRepository.save(artist);

        podcast1 =  new Podcast("title", "5:13", artist, 1,5, "comedy");
        podcast2 =  new Podcast("new title 2", "3:14", artist, 2, 6, "philosophy");
        podcastsList.add(podcast1);
        podcastsList.add(podcast2);

        podcastRepository.saveAll(podcastsList);
    }

    @AfterEach
    void tearsDown(){
        podcastRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    public void savePodcastCorrectInfoTest() throws Exception {
        Podcast podcast =  new Podcast("new title 3", "2:15", null, 1, 4, "comedy");
        Artist artist = new Artist();
        artist.setUsername("julia");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(artist.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        artistRepository.save(artist);
        podcast.setArtist(artist);

        String expectedJson = objectMapper.writeValueAsString(new AudioGeneralInfoDTO(podcast));
        AudioGeneralInfoDTO result = podcastService.savePodcast(podcast);
        String resultJson = objectMapper.writeValueAsString(result);

        assertEquals(3, podcastRepository.count());
        assertEquals(expectedJson, resultJson);
    }

    @Test
    public void savePodcastWrongDurationFormatTest() throws Exception {
        Podcast podcast =  new Podcast("title", "2a15", null, 3, 5, "comedy");
        Artist artist = new Artist();
        artist.setUsername("julia");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(artist.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        artistRepository.save(artist);
        podcast.setArtist(artist);

        assertThrows(BadRequestFormatException.class, () -> {podcastService.savePodcast(podcast);});
    }

    @Test
    public void savePodcastEmptyTitleTest() throws Exception {
        Podcast podcast =  new Podcast("", "2:15", null, 1,6, "");
        Artist artist = new Artist();
        artist.setUsername("julia");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(artist.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        artistRepository.save(artist);
        podcast.setArtist(artist);

        assertThrows(ConstraintViolationException.class, () -> {podcastService.savePodcast(podcast);});
    }

    @Test
    public void deletePodcastExistingIdTest(){
        Artist artist = new Artist(new User(null, "artist", "ju", "1234", true, new ArrayList<>(), null));
        artistRepository.save(artist);

        Podcast podcast =  new Podcast("title", "2:15", artist, 1,5, "culture");
        podcastRepository.save(podcast);
        Long id = podcast.getId();

        assertTrue(podcastRepository.findById(id).isPresent());
        podcastService.deletePodcast(id);
        assertFalse(podcastRepository.findById(id).isPresent());
    }

    @Test
    public void deletePodcastNotExistingIdTest(){
        assertThrows(ResourceNotFoundException.class, () -> {
            podcastService.deletePodcast(45L);});
    }

    @Test
    public void getAllPodcastsTest() throws Exception{
        List<AudioGeneralInfoDTO> audioGeneralInfoDTOS = new ArrayList<>();
        for(Podcast podcast : podcastsList){
            audioGeneralInfoDTOS.add(new AudioGeneralInfoDTO(podcast));
        }
        String expectedJson = objectMapper.writeValueAsString(audioGeneralInfoDTOS);
        String resultJson = objectMapper.writeValueAsString(podcastService.getAllPodcasts());
        assertEquals(expectedJson, resultJson);
    }

    @Test
    public void getPodcastByTitleExistingPodcastTest() throws Exception{
        List<Podcast> podcasts = new ArrayList<>();
        podcasts.add(podcast2);
        List<AudioGeneralInfoDTO> audioGeneralInfoDTOS = new ArrayList<>();
        for(Podcast podcast : podcasts){
            audioGeneralInfoDTOS.add(new AudioGeneralInfoDTO(podcast));
        }
        String expectedJson = objectMapper.writeValueAsString(audioGeneralInfoDTOS);
        String resultJson = objectMapper.writeValueAsString(podcastService.getPodcastByTitle("new"));
        assertEquals(expectedJson, resultJson);
    }

    @Test
    public void getPodcastByTitleNotExistingPodcastTest() throws Exception{
        assertThrows(ResourceNotFoundException.class, () -> {
            podcastService.getPodcastByTitle("wrong title");});
    }

    @Test
    public void getPodcastByArtistNameExistingPodcastTest() throws Exception{
        List<Podcast> podcasts = new ArrayList<>();
        podcasts.add(podcast1);
        podcasts.add(podcast2);
        List<AudioGeneralInfoDTO> audioGeneralInfoDTOS = new ArrayList<>();
        for(Podcast podcast : podcasts){
            audioGeneralInfoDTOS.add(new AudioGeneralInfoDTO(podcast));
        }
        String expectedJson = objectMapper.writeValueAsString(audioGeneralInfoDTOS);
        String resultJson = objectMapper.writeValueAsString(podcastService.getPodcastByArtistName("podcaster"));
        assertEquals(expectedJson, resultJson);
    }

    @Test
    public void getPodcastByArtistNameNotExistingPodcastTest() throws Exception{
        assertThrows(ResourceNotFoundException.class, () -> {
            podcastService.getPodcastByArtistName("wrong artist");});
    }

}
