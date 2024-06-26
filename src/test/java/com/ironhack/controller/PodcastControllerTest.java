package com.ironhack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.dto.PodcastGeneralInfoDTO;
import com.ironhack.security.utils.ArtistStatus;
import com.ironhack.security.model.Artist;
import com.ironhack.security.model.Role;
import com.ironhack.security.model.User;
import com.ironhack.security.repository.ArtistRepository;
import com.ironhack.security.repository.RoleRepository;
import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.model.Podcast;
import com.ironhack.repository.PodcastRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class PodcastControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private PodcastRepository podcastRepository;
    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private RoleRepository roleRepository;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Artist artist;

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        artist = new Artist(new User(null, "artist", "artist", "1234",
                true, ArtistStatus.ACTIVE, new ArrayList<>(), null));
        Collection<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("ROLE_USER").get());
        roles.add(roleRepository.findByName("ROLE_ARTIST").get());
        artist.setRoles(roles);
        artistRepository.save(artist);

        Podcast podcast1 =  new Podcast("podcast title 1", "34:14", artist, 3, 5, "pop");
        Podcast podcast2 =  new Podcast("podcast title 2", "45:14", artist, 2, 4, "pop");
        podcastRepository.save(podcast1);
        podcastRepository.save(podcast2);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(artist.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    void tearDown() {
        podcastRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    public void savePodcastCorrectInfoTest() throws Exception{
        Podcast podcast3 =  new Podcast("Podcast title 3", "3:14", artist, 4, 6, "culture");
        String podcastJson = objectMapper.writeValueAsString(podcast3);
        String expectedJson = objectMapper.writeValueAsString(new AudioGeneralInfoDTO(podcast3));

        mockMvc.perform(post("/api/artist/podcast")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(podcastJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void savePodcastWrongDurationFormatTest() throws Exception{
        Podcast podcast3 =  new Podcast("podcast title 3", "3:::14", artist, 4, 5, "culture");
        String podcastJson = objectMapper.writeValueAsString(podcast3);
        String expectedJson = objectMapper.writeValueAsString(new PodcastGeneralInfoDTO(podcast3));

        mockMvc.perform(post("/api/artist/podcast")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(podcastJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Bad request. Duration has not a correct format: HH:MM:SS or MM:SS or SS")));
    }

    @Test
    public void savePodcastEmptyTitleTest() throws Exception{
        Podcast podcast3 =  new Podcast("", "3:14", artist, 3, 5, "comedy");
        String podcastJson = objectMapper.writeValueAsString(podcast3);
        String expectedJson = objectMapper.writeValueAsString(new PodcastGeneralInfoDTO(podcast3));

        mockMvc.perform(post("/api/artist/podcast")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(podcastJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Bad request. Title is required.")));
    }

    @Test
    public void deletePodcastByTitleTest() throws Exception{
        mockMvc.perform(delete("/api/artist/podcast/{title}", "podcast title 1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deletePodcastByTitleWrongTitleTest() throws Exception{
        mockMvc.perform(delete("/api/artist/podcast/{title}", "wrong title")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("Podcast with title \"wrong title\" not found")));
    }
    @Test
    void getAllPodcastsTest() throws Exception {
        List<Podcast> podcasts = podcastRepository.findAll();
        List<PodcastGeneralInfoDTO> audioGeneralInfoDTOS = new ArrayList<>();
        for(Podcast podcast : podcasts){
            audioGeneralInfoDTOS.add(new PodcastGeneralInfoDTO(podcast));
        }
        String expectedJson = objectMapper.writeValueAsString(audioGeneralInfoDTOS);

        mockMvc.perform(get("/api/user/podcasts").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }
    @Test
    void getPodcastsByTitleTest() throws Exception {
        List<Podcast> podcasts = podcastRepository.findByTitleContaining("title");
        List<PodcastGeneralInfoDTO> audioGeneralInfoDTOS = new ArrayList<>();
        for(Podcast podcast : podcasts){
            audioGeneralInfoDTOS.add(new PodcastGeneralInfoDTO(podcast));
        }
        String expectedJson = objectMapper.writeValueAsString(audioGeneralInfoDTOS);
        mockMvc.perform(get("/api/user/podcast/title/{title}", "title")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void getPodcastsByTitleNotExistingTitleTest() throws Exception {
        mockMvc.perform(get("/api/user/podcast/title/{title}", "wrong")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("No podcasts found with that title.")));
    }

    @Test
    void getPodcastsByArtistNameTest() throws Exception {
        List<Podcast> podcasts= podcastRepository.findByArtistNameContaining("artist");
        List<PodcastGeneralInfoDTO> audioGeneralInfoDTOS = new ArrayList<>();
        for(Podcast podcast : podcasts){
            audioGeneralInfoDTOS.add(new PodcastGeneralInfoDTO(podcast));
        }
        String expectedJson = objectMapper.writeValueAsString(audioGeneralInfoDTOS);
        mockMvc.perform(get("/api/user/podcast/artist_name/{artistName}", "artist")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void getPodcastsByArtistNameNotExistingArtistTest() throws Exception {
        mockMvc.perform(get("/api/user/podcast/artist_name/{artistName}", "wrong")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("No podcasts found with that artist name.")));
    }

    @Test
    void getPodcastsByAllInfoTest() throws Exception {
        List<PodcastGeneralInfoDTO> audioGeneralInfoDTOS = new ArrayList<>();
        List<Podcast> podcasts = podcastRepository.findAll();
        for(Podcast podcast : podcasts){
            audioGeneralInfoDTOS.add(new PodcastGeneralInfoDTO(podcast));
        }
        String expectedJson = objectMapper.writeValueAsString(audioGeneralInfoDTOS);
        mockMvc.perform(get("/api/user/podcast/{info}", "pod")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void getPodcastsByAllInfoWrongInfoTest() throws Exception {
        mockMvc.perform(get("/api/user/podcast/{info}", "wrong info")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("No podcasts found with that info.")));
    }
}
