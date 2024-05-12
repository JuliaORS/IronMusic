package com.ironhack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.security.utils.ArtistStatus;
import com.ironhack.security.model.Artist;
import com.ironhack.security.model.Role;
import com.ironhack.security.model.User;
import com.ironhack.security.repository.ArtistRepository;
import com.ironhack.security.repository.RoleRepository;
import com.ironhack.security.repository.UserRepository;
import com.ironhack.security.service.impl.UserService;
import com.ironhack.model.Playlist;
import com.ironhack.model.Audio;
import com.ironhack.repository.PlaylistRepository;
import com.ironhack.repository.AudioRepository;
import com.ironhack.service.impl.PlaylistService;
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

import static org.junit.jupiter.api.Assertions.*;
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
    private PlaylistService playlistService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserService userService;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Artist artist;

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

        String expectedJson = objectMapper.writeValueAsString(newPlaylist.getName());

        mockMvc.perform(post("/api/user/playlist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PlaylistJson))
                .andExpect(status().isCreated())
                .andExpect(content().string("new Playlist title"));
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

        String expectedJson = objectMapper.writeValueAsString(newPlaylist.getName());

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
                .andExpect(content().string(Matchers.containsString("Playlist with name \"wrong playlist\" not found")));
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
        mockMvc.perform(delete("/api/user/playlist/{playlistName}/audio/{audioTitle}", "summer_hits", "audio title 1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void removeAudioFromPlaylistWrongPlaylistTitleTest() throws Exception {
        mockMvc.perform(delete("/api/user/playlist/{playlistName}/audio/{audioTitle}", "wrong playlist", "audio title 1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("Playlist with name \"wrong playlist\" not found")));
    }

    @Test
    public void removeAudioFromPlaylistWrongAudioTitleTest() throws Exception {
        mockMvc.perform(delete("/api/user/playlist/{playlistName}/audio/{audioTitle}", "summer_hits", "wrong audio")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("Audio with title \"wrong audio\" not found")));
    }

    @Test
    public void addUserToPlaylistByUserNameTest() throws Exception {
        User user2 = new User(null, "user2", "username2", "1234",
                true, ArtistStatus.INACTIVE, null, null);
        userRepository.save(user2);

        mockMvc.perform(put("/api/user/playlist/{playlistName}/user/{username}", "summer_hits", "username2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(2, playlistRepository.findByName("summer_hits").get(0).getUsers().size());
    }

    @Test
    public void addUserToPlaylistBytNameUserWrongPlaylistNameTest() throws Exception {
        User user2 = new User(null, "user2", "username2", "1234",
                true, ArtistStatus.INACTIVE, null, null);
        userRepository.save(user2);

        mockMvc.perform(put("/api/user/playlist/{playlistName}/user/{userName}", "wrong playlist", "username2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("Playlist with name \"wrong playlist\" not found")));
    }

    @Test
    public void addUserToPlaylistBytNameUserWrongUserNameTest() throws Exception {
        mockMvc.perform(put("/api/user/playlist/{playlistName}/user/{username}", "summer_hits", "wrong user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("User with username \"wrong user\" not found")));
    }

    @Test
    public void removeUserFromPlaylistTest() throws Exception {
        User user2 = new User(null, "user2", "username2", "1234",
                true, ArtistStatus.INACTIVE, null, null);
        userRepository.save(user2);
        playlistService.addUserToPlaylistByUsername("summer_hits", "username2");
        assertEquals(2, playlistRepository.findByName("summer_hits").get(0).getUsers().size());

        mockMvc.perform(delete("/api/user/playlist/{playlistName}/user/{username}", "summer_hits", "username2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(1, playlistRepository.findByName("summer_hits").get(0).getUsers().size());
    }

    @Test
    public void removeUserFromPlaylistWrongPlaylistNameTest() throws Exception {
        User user2 = new User(null, "user2", "username2", "1234",
                true, ArtistStatus.INACTIVE, null, null);
        userRepository.save(user2);
        playlistService.addUserToPlaylistByUsername("summer_hits", "username2");

        mockMvc.perform(delete("/api/user/playlist/{playlistName}/user/{username}", "wrong playlist", "username2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("Playlist with name \"wrong playlist\" not found")));
    }

    @Test
    public void removeUserFromPlaylistWrongUserNameTest() throws Exception {
        mockMvc.perform(delete("/api/user/playlist/{playlistName}/user/{username}", "summer_hits", "wrong user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("User with username \"wrong user\" not found")));
    }

    @Test
    public void getAllAudiosFromPlaylistTest() throws Exception{
        List<Audio> audioList = new ArrayList<>();
        audioList.add(audioRepository.findByTitle("audio title 1").get(0));
        List<AudioGeneralInfoDTO> audioGeneralInfoDTOS = new ArrayList<>();
        for(Audio audio : audioList){
            audioGeneralInfoDTOS.add(new AudioGeneralInfoDTO(audio));
        }
        String expectedJson = objectMapper.writeValueAsString(audioGeneralInfoDTOS);

        mockMvc.perform(get("/api/user/playlist/{playlistName}", "summer_hits")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void getAllAudiosFromPlaylistNotExistingTest() throws Exception{
        mockMvc.perform(get("/api/user/playlist/{playlistName}", "wrong")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("Playlist with name \"wrong\" not found")));
    }

    @Test
    public void getAllAudiosFromPlaylistEmptyPlaylistTest() throws Exception{
        playlistService.removeAudioFromPlaylistByTitle("summer_hits", "audio title 1");
        mockMvc.perform(get("/api/user/playlist/{playlistName}", "summer_hits")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("Playlist with name \"summer_hits\" is empty")));
    }

    @Test
    public void getAllAudiosFromPublicPlaylistTest() throws Exception{
        User user2 = new User(null, "user2", "username2", "1234",
                true, ArtistStatus.INACTIVE, null, null);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(user2.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Playlist playlist = playlistRepository.findByName("summer_hits").get(0);
        playlist.setPrivate(false);
        playlistRepository.save(playlist);
        assertFalse(playlistRepository.findByName("summer_hits").get(0).isPrivate());
        List<Audio> audioList = new ArrayList<>();
        audioList.add(audioRepository.findByTitle("audio title 1").get(0));
        List<AudioGeneralInfoDTO> audioGeneralInfoDTOS = new ArrayList<>();
        for(Audio audio : audioList){
            audioGeneralInfoDTOS.add(new AudioGeneralInfoDTO(audio));
        }
        String expectedJson = objectMapper.writeValueAsString(audioGeneralInfoDTOS);

        mockMvc.perform(get("/api/user/public_playlist/{playlistName}", "summer_hits")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void getAllAudiosFromPublicPlaylistNotExistingTest() throws Exception{
        User user2 = new User(null, "user2", "username2", "1234",
                true, ArtistStatus.INACTIVE, null, null);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(user2.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        mockMvc.perform(get("/api/user/public_playlist/{playlistName}", "wrong")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("Playlist with name \"wrong\" not found")));
    }

    @Test
    public void getAllAudiosFromPublicPlaylistEmptyPlaylistTest() throws Exception{
        playlistService.removeAudioFromPlaylistByTitle("summer_hits", "audio title 1");
        playlistService.makePlaylistPublic("summer_hits");
        mockMvc.perform(get("/api/user/public_playlist/{playlistName}", "summer_hits")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("Playlist with name \"summer_hits\" is empty")));
    }

    @Test
    public void makePlaylistPublicTest() throws Exception{
        assertTrue(playlistRepository.findByName("summer_hits").get(0).isPrivate());
        mockMvc.perform(put("/api/user/playlist/{playlistName}", "summer_hits")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertFalse(playlistRepository.findByName("summer_hits").get(0).isPrivate());
    }

    @Test
    public void makePlaylistPublicNotExistingTest() throws Exception{
        mockMvc.perform(put("/api/user/playlist/{playlistName}", "wrong")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("Playlist with name \"wrong\" not found")));
    }

    @Test
    public void makePlaylistPublicAlreadyPublicTest() throws Exception{
        playlistService.makePlaylistPublic("summer_hits");
        mockMvc.perform(put("/api/user/playlist/{playlistName}", "summer_hits")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Playlist is already public")));
    }
}
