package com.ironhack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.demosecurityjwt.security.Utils.ArtistStatus;
import com.ironhack.demosecurityjwt.security.models.Artist;
import com.ironhack.demosecurityjwt.security.models.Role;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.repositories.ArtistRepository;
import com.ironhack.demosecurityjwt.security.repositories.RoleRepository;
import com.ironhack.demosecurityjwt.security.repositories.UserRepository;
import com.ironhack.dto.PlaylistGeneralInfoDTO;
import com.ironhack.model.Playlist;
import com.ironhack.model.Audio;
import com.ironhack.repository.PlaylistRepository;
import com.ironhack.repository.AudioRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class PlaylistControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private AudioRepository audioRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Artist artist;
    private Playlist playlist;
    private User user;

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        artist = new Artist(new User(null, "artist", "artist", "1234",
                true, ArtistStatus.ACTIVE, new ArrayList<>(), null));
        artistRepository.save(artist);

        Audio audio1 =  new Audio("audio title 1", "5:13", artist);
        audioRepository.save(audio1);
        List<Audio> audioList = new ArrayList<>();
        audioList.add(audio1);

        Collection<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("ROLE_USER").get());
        user = new User(null, "user1", "username1", "1234",
                true, ArtistStatus.INACTIVE, roles, null);
        userRepository.save(user);

        List<User> userList = new ArrayList<>();
        userList.add(user);

        Playlist playlist = new Playlist("summer_hits", audioList, userList);
        playlistRepository.save(playlist);

        List<Playlist> playlistList = new ArrayList<>();
        playlistList.add(playlist);
        user.setPlaylists(playlistList);
        userRepository.save(user);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(user.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    void tearsDown(){
        userRepository.deleteAll();
        artistRepository.deleteAll();
        audioRepository.deleteAll();
        playlistRepository.deleteAll();
    }

    @Test
    public void savePlaylistCorrectInfoTest () throws Exception {
        Collection<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("ROLE_USER").get());
        User newUser = new User(null, "user2", "username2", "1234",
                true, ArtistStatus.INACTIVE, roles, null);
        userRepository.save(newUser);

        Playlist newPlaylist =  new Playlist("new Playlist title", null, null);
        String PlaylistJson = objectMapper.writeValueAsString(newPlaylist);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(newUser.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String expectedJson = objectMapper.writeValueAsString(new PlaylistGeneralInfoDTO(newPlaylist));

        mockMvc.perform(post("/api/user/playlist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PlaylistJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void savePlaylistEmptyNameTest() throws Exception{
        Collection<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("ROLE_USER").get());
        User user = new User(null, "user2", "username2", "1234",
                true, ArtistStatus.INACTIVE, roles, null);
        userRepository.save(user);


        Playlist newPlaylist =  new Playlist("", null, null);
        String PlaylistJson = objectMapper.writeValueAsString(newPlaylist);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(user.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String expectedJson = objectMapper.writeValueAsString(new PlaylistGeneralInfoDTO(newPlaylist));

        mockMvc.perform(post("/api/user/playlist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PlaylistJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Bad request. Playlist name is required.")));
    }

    @Test
    public void deletePlaylistByNameTest() throws Exception{
        mockMvc.perform(delete("/api/user/playlist/{name}", "summer_hits")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deletePlaylistByNameWrongNameTest() throws Exception{
        mockMvc.perform(delete("/api/user/playlist/{name}", "wrong name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("Playlist with name \"wrong name\" not found")));
    }

    @Test
    public void addAudioToPlaylistBytTitleAudioTest() throws Exception {
        Audio audio3 =  new Audio("audio title 3", "3:14", artist);
        audioRepository.save(audio3);
        mockMvc.perform(put("/api/user/playlist/{playlistName}/audio/{audioTitle}", "summer_hits", "audio title 3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void addAudioToPlaylistBytTitleAudioWrongPlaylistTitleTest() throws Exception {
        Audio audio3 =  new Audio("audio title 3", "3:14", artist);
        audioRepository.save(audio3);
        mockMvc.perform(put("/api/user/playlist/{playlistName}/audio/{audioTitle}", "wrong playlist", "audio title 3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("Playlist with title \"wrong playlist\" not found")));
    }

    @Test
    public void addAudioToPlaylistBytTitleAudioWrongAudioTitleTest() throws Exception {
        mockMvc.perform(put("/api/user/playlist/{playlistName}/audio/{audioTitle}", "summer_hits", "wrong audio")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("Audio with title \"wrong audio\" not found")));
    }

    @Test
    public void removeAudioFromPlaylistTest() throws Exception {
        mockMvc.perform(put("/api/user/playlist/{playlistName}/audio/{audioTitle}", "summer_hits", "audio title 1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void removeAudioFromPlaylistWrongPlaylistTitleTest() throws Exception {
        mockMvc.perform(put("/api/user/playlist/{playlistName}/audio/{audioTitle}", "wrong playlist", "audio title 1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("Playlist with title \"wrong playlist\" not found")));
    }

    @Test
    public void removeAudioFromPlaylistWrongAudioTitleTest() throws Exception {
        mockMvc.perform(put("/api/user/playlist/{playlistName}/audio/{audioTitle}", "summer_hits", "wrong audio")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("Audio with title \"wrong audio\" not found")));
    }
}
