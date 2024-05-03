package com.ironhack.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.demosecurityjwt.security.models.Artist;
import com.ironhack.demosecurityjwt.security.models.Role;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.repositories.ArtistRepository;
import com.ironhack.demosecurityjwt.security.repositories.RoleRepository;
import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.exceptions.BadRequestFormatException;
import com.ironhack.exceptions.ResourceNotFoundException;
import com.ironhack.model.Podcast;
import com.ironhack.model.Podcast;
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
import java.util.Collection;
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

    @Autowired
    private RoleRepository roleRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<Podcast> podcastsList = new ArrayList<>();
    private Podcast podcast1;
    private Podcast podcast2;
    private Artist artist;

    @BeforeEach
    void setUp(){
        artist = new Artist(new User(null, "podcaster", "julia", "1234", true, new ArrayList<>(), null));
        artistRepository.save(artist);

        podcast1 =  new Podcast("podcast title 1", "5:13", artist, 1,5, "comedy");
        podcast2 =  new Podcast("podcast title 2", "3:14", artist, 2, 6, "philosophy");
        podcastsList.add(podcast1);
        podcastsList.add(podcast2);

        podcastRepository.saveAll(podcastsList);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(artist.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }

    @AfterEach
    void tearsDown(){
        podcastRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    public void savePodcastCorrectInfoTest() throws Exception {
        Podcast podcast =  new Podcast("new title 3", "2:15", null, 1, 4, "comedy");
        Artist newArtist = new Artist(new User(null, "podcaster1", "ju", "1234", true, new ArrayList<>(), null));
        Collection<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("ROLE_USER"));
        roles.add(roleRepository.findByName("ROLE_ARTIST"));
        newArtist.setRoles(roles);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(newArtist.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        artistRepository.save(newArtist);
        podcast.setArtist(newArtist);

        String expectedJson = objectMapper.writeValueAsString(new AudioGeneralInfoDTO(podcast));
        AudioGeneralInfoDTO result = podcastService.savePodcast(podcast);
        String resultJson = objectMapper.writeValueAsString(result);

        assertEquals(3, podcastRepository.count());
        assertEquals(expectedJson, resultJson);
    }

    @Test
    public void savePodcastWrongDurationFormatTest() throws Exception {
        Podcast podcast =  new Podcast("title", "2a15", null, 3, 5, "comedy");
        Artist newArtist = new Artist(new User(null, "podcaster1", "ju", "1234", true, new ArrayList<>(), null));
        Collection<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("ROLE_USER"));
        roles.add(roleRepository.findByName("ROLE_ARTIST"));
        newArtist.setRoles(roles);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(newArtist.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        artistRepository.save(newArtist);
        podcast.setArtist(newArtist);

        assertThrows(BadRequestFormatException.class, () -> {podcastService.savePodcast(podcast);});
    }

    @Test
    public void savePodcastEmptyTitleTest() throws Exception {
        Podcast podcast =  new Podcast("", "2:15", null, 3, 5, "comedy");
        Artist newArtist = new Artist(new User(null, "podcaster1", "ju", "1234", true, new ArrayList<>(), null));
        Collection<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("ROLE_USER"));
        roles.add(roleRepository.findByName("ROLE_ARTIST"));
        newArtist.setRoles(roles);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(newArtist.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        artistRepository.save(newArtist);
        podcast.setArtist(newArtist);

        assertThrows(ConstraintViolationException.class, () -> {podcastService.savePodcast(podcast);});
    }

    @Test
    public void deletePodcastExistingTitleTest(){
        assertFalse(podcastRepository.findByTitle("podcast title 1").isEmpty());
        podcastService.deletePodcastByTitle("podcast title 1");
        assertTrue(podcastRepository.findByTitle("podcast title 1").isEmpty());
    }

    @Test
    public void deletePodcastNotExistingTitleTest(){
        assertThrows(ResourceNotFoundException.class, () -> {
            podcastService.deletePodcastByTitle("wrong title");});
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
        String resultJson = objectMapper.writeValueAsString(podcastService.getPodcastByTitle("2"));
        assertEquals(expectedJson, resultJson);
    }

    @Test
    public void getPodcastByTitleNotExistingPodcastTest() throws Exception{
        assertThrows(ResourceNotFoundException.class, () -> {
            podcastService.getPodcastByTitle("wrong title");});
    }

    @Test
    public void getPodcastByArtistNameExistingPodcastTest() throws Exception{
        List<AudioGeneralInfoDTO> audioGeneralInfoDTOS = new ArrayList<>();
        audioGeneralInfoDTOS.add(new AudioGeneralInfoDTO(podcast1));
        audioGeneralInfoDTOS.add(new AudioGeneralInfoDTO(podcast2));
        String expectedJson = objectMapper.writeValueAsString(audioGeneralInfoDTOS);
        String resultJson = objectMapper.writeValueAsString(podcastService.getPodcastByArtistName("podcaster"));
        assertEquals(expectedJson, resultJson);
    }

    @Test
    public void getPodcastByArtistNameNotExistingPodcastTest() throws Exception{
        assertThrows(ResourceNotFoundException.class, () -> {
            podcastService.getPodcastByArtistName("wrong artist");});
    }

    @Test
    public void getPodcastByAllInfoExistingInfoTest() throws Exception{
        List<AudioGeneralInfoDTO> audioGeneralInfoDTOS = new ArrayList<>();
        audioGeneralInfoDTOS.add(new AudioGeneralInfoDTO(podcast2));

        String expectedJson = objectMapper.writeValueAsString(audioGeneralInfoDTOS);
        String resultJson = objectMapper.writeValueAsString(podcastService.getPodcastByAllInfo("2"));
        assertEquals(expectedJson, resultJson);
    }

    @Test
    public void getPodcastByAllInfoNotExistingInfoTest() throws Exception{
        assertThrows(ResourceNotFoundException.class, () -> {
            podcastService.getPodcastByAllInfo("wrong");});
    }

}
