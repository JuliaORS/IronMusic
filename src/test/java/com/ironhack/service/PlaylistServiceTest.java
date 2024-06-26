package com.ironhack.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.exception.MakePlaylistPublicException;
import com.ironhack.security.utils.ArtistStatus;
import com.ironhack.security.exception.UserNotFoundException;
import com.ironhack.security.model.Artist;
import com.ironhack.security.model.Role;
import com.ironhack.security.model.User;
import com.ironhack.security.repository.ArtistRepository;
import com.ironhack.security.repository.RoleRepository;
import com.ironhack.security.repository.UserRepository;
import com.ironhack.exception.ResourceNotFoundException;
import com.ironhack.model.Playlist;
import com.ironhack.model.Audio;
import com.ironhack.repository.PlaylistRepository;
import com.ironhack.repository.AudioRepository;
import com.ironhack.service.impl.PlaylistService;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PlaylistServiceTest {
    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AudioRepository audioRepository;
    @Autowired
    private RoleRepository roleRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private User user;
    private Artist artist;

    @BeforeEach
    void setUp(){
        artist = new Artist(new User(null, "artist", "ju", "1234",
                true, ArtistStatus.ACTIVE,  new ArrayList<>(), null));
        artistRepository.save(artist);

        Audio audio = new Audio("audio title", "3:34", artist);
        audioRepository.save(audio);
        List<Audio> audiosList = new ArrayList<>();
        audiosList.add(audio);

        Collection<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("ROLE_USER").get());

        User user = new User(null, "user1", "username1", "1234",
                true, ArtistStatus.INACTIVE, roles, null);
        userRepository.save(user);

        List<User> userList = new ArrayList<>();
        userList.add(user);

        Playlist playlist = new Playlist("summer hits", audiosList, userList);
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
    public void savePlaylistCorrectInfoTest() throws Exception {
        Playlist newPlaylist =  new Playlist("new playlist title", null, null);

        String expectedJson = objectMapper.writeValueAsString(newPlaylist.getName());
        String result = playlistService.savePlaylist(newPlaylist);
        String resultJson = objectMapper.writeValueAsString(result);

        assertEquals(2, playlistRepository.count());
        assertEquals(expectedJson, resultJson);
    }

    @Test
    public void savePlaylistEmptyTitleTest() throws Exception {
        Playlist newPlaylist =  new Playlist("", null, null);
        assertThrows(ConstraintViolationException.class, () -> {playlistService.savePlaylist(newPlaylist);});
    }

    @Test
    public void deletePlaylistByNameExistingNameTest(){
        assertFalse(playlistRepository.findByName("summer hits").isEmpty());
        assertEquals(1, audioRepository.count());
        playlistService.deletePlaylistByName("summer hits");
        assertTrue(playlistRepository.findAll().isEmpty());
        assertEquals(1, audioRepository.count());
    }

    @Test
    public void deletePlaylistNotExistingTitleTest(){
        assertThrows(ResourceNotFoundException.class, () -> {
            playlistService.deletePlaylistByName("wrong name");});
    }

    @Test
    public void addAudioToPlaylistTest(){
          Audio audio3 =  new Audio("Audio title 3", "2:14", artist);
          audioRepository.save(audio3);

          assertEquals(2, audioRepository.count());
          assertEquals(1, playlistRepository.findByName("summer hits").get(0).getAudios().size());

          playlistService.addAudioToPlaylistByTitle("summer hits", "Audio title 3");

          assertEquals(2, playlistRepository.findByName("summer hits").get(0).getAudios().size());
    }

    @Test
    public void addAudioToPlaylistNotExistingPlaylistTest(){
      assertThrows(ResourceNotFoundException.class, () -> {playlistService.addAudioToPlaylistByTitle(
              "wrong Playlist", "audio title");});
    }

    @Test
    public void addAudioToPlaylistNotExistingAudioTest(){
      assertThrows(ResourceNotFoundException.class, () -> {playlistService.addAudioToPlaylistByTitle(
              "summer hits", "wrong title");});
    }

    @Test
    public void removeAudioFromPlaylistByTitleTest(){
        assertEquals(1, playlistRepository.findByName("summer hits").get(0).getAudios().size());

        playlistService.removeAudioFromPlaylistByTitle("summer hits", "audio title");

        assertEquals(0, playlistRepository.findByName("summer hits").get(0).getAudios().size());
        assertFalse(audioRepository.findByTitle("audio title").isEmpty()); //Audio still exists
    }

    @Test
    public void removeAudioFromPlaylistNotExistingPlaylistTest(){
      assertThrows(ResourceNotFoundException.class, () -> {playlistService.removeAudioFromPlaylistByTitle(
              "wrong", "audio title");});
    }

    @Test
    public void removeAudioFromPlaylistNotExistingAudioTest(){
    assertThrows(ResourceNotFoundException.class, () -> {playlistService.removeAudioFromPlaylistByTitle(
            "summer hits", "wrong");});
    }

    @Test
    public void addUserToPlaylistTest(){
        User user2 = new User(null, "user2", "username2", "1234",
                true, ArtistStatus.INACTIVE, null, null);
        userRepository.save(user2);

        assertEquals(1, playlistRepository.findByName("summer hits").get(0).getUsers().size());
        playlistService.addUserToPlaylistByUsername("summer hits", "username2");
        assertEquals(2, playlistRepository.findByName("summer hits").get(0).getUsers().size());
    }

    @Test
    public void addUserToPlaylistNotExistingPlaylistTest(){
        User user2 = new User(null, "user2", "username2", "1234",
                true, ArtistStatus.INACTIVE, null, null);
        userRepository.save(user2);

        assertThrows(ResourceNotFoundException.class, () -> {playlistService.addUserToPlaylistByUsername(
                "wrong Playlist", "username2");});
    }

    @Test
    public void addUserToPlaylistNotExistingUserTest(){
        assertThrows(UserNotFoundException.class, () -> {playlistService.addUserToPlaylistByUsername(
                "summer hits", "wrong name");});
    }

    @Test
    public void removeUserFromPlaylistByNameTest(){
        User user2 = new User(null, "user2", "username2", "1234",
                true, ArtistStatus.INACTIVE, null, null);
        userRepository.save(user2);
        playlistService.addUserToPlaylistByUsername("summer hits", "username2");

        assertEquals(2, playlistRepository.findByName("summer hits").get(0).getUsers().size());

        playlistService.removeUserFromPlaylistByUsername("summer hits", "username2");

        assertEquals(1, playlistRepository.findByName("summer hits").get(0).getUsers().size());
        assertFalse(userRepository.findByUsername("username2").isEmpty()); //User still exists
    }

    @Test
    public void removeUserFromPlaylistNotExistingPlaylistTest(){
        assertThrows(ResourceNotFoundException.class, () -> {playlistService.removeUserFromPlaylistByUsername(
                "wrong", "username1");});
    }

    @Test
    public void removeUserFromPlaylistNotExistingUserTest(){
        assertThrows(UserNotFoundException.class, () -> {playlistService.removeUserFromPlaylistByUsername(
                "summer hits", "wrong");});
    }

    @Test
    public void getAllAudiosFromPlaylistTest() throws Exception{
        List<Audio> audioList = new ArrayList<>();
        audioList.add(audioRepository.findByTitle("audio title").get(0));

        List<AudioGeneralInfoDTO> audioGeneralInfoDTOS = new ArrayList<>();
        for(Audio audio : audioList){
            audioGeneralInfoDTOS.add(new AudioGeneralInfoDTO(audio));
        }
        String expectedJson = objectMapper.writeValueAsString(audioGeneralInfoDTOS);
        String resultJson = objectMapper.writeValueAsString(playlistService.getAllAudiosFromPlaylist("summer hits"));
        assertEquals(expectedJson, resultJson);
    }

    @Test
    public void getAllAudiosFromPlaylistNotExistingTest() throws Exception{
        assertThrows(ResourceNotFoundException.class, () -> {playlistService.getAllAudiosFromPlaylist(
                "wrong");});
    }

    @Test
    public void getAllAudiosFromPlaylistEmptyPlaylistTest() throws Exception{
        Playlist newPlaylist =  new Playlist("new playlist title", null, null);
        playlistRepository.save(newPlaylist);
        assertThrows(ResourceNotFoundException.class, () -> {playlistService.getAllAudiosFromPlaylist(
                "new playlist title");});
    }

    @Test
    public void getAllAudiosFromPublicPlaylistTest() throws Exception{
        Playlist playlist = playlistRepository.findByName("summer hits").get(0);
        playlist.setPrivate(false);
        playlistRepository.save(playlist);

        List<Audio> audioList = new ArrayList<>();
        audioList.add(audioRepository.findByTitle("audio title").get(0));

        List<AudioGeneralInfoDTO> audioGeneralInfoDTOS = new ArrayList<>();
        for(Audio audio : audioList){
            audioGeneralInfoDTOS.add(new AudioGeneralInfoDTO(audio));
        }
        String expectedJson = objectMapper.writeValueAsString(audioGeneralInfoDTOS);
        String resultJson = objectMapper.writeValueAsString(playlistService
                .getAllAudiosFromPublicPlaylist("summer hits"));
        assertEquals(expectedJson, resultJson);
    }

    @Test
    public void getAllAudiosFromPublicPlaylistNotExistingTest() throws Exception{
        User user2 = new User(null, "user2", "username2", "1234",
                true, ArtistStatus.INACTIVE, null, null);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(user2.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        assertThrows(ResourceNotFoundException.class, () -> {playlistService.getAllAudiosFromPublicPlaylist(
                "summer hits");});
    }

    @Test
    public void getAllAudiosFromPublicPlaylistEmptyPlaylistTest() throws Exception{
        Playlist newPlaylist =  new Playlist("new playlist title", null, null);
        newPlaylist.setPrivate(false);
        playlistRepository.save(newPlaylist);
        assertThrows(ResourceNotFoundException.class, () -> {playlistService.getAllAudiosFromPlaylist(
                "new playlist title");});
    }

    @Test
    public void makePlaylistPublicTest() throws Exception{
        assertTrue(playlistRepository.findByName("summer hits").get(0).isPrivate());
        playlistService.makePlaylistPublic("summer hits");
        assertFalse(playlistRepository.findByName("summer hits").get(0).isPrivate());
    }

    @Test
    public void makePlaylistPublicNotExistingTest() throws Exception{
        playlistRepository.findByName("summer hits").get(0).setPrivate(false);
        playlistRepository.save(playlistRepository.findByName("summer hits").get(0));
        assertThrows(ResourceNotFoundException.class, () -> {playlistService.makePlaylistPublic(
                "wrong");});
    }

    @Test
    public void makePlaylistPublicAlreadyPublicTest() throws Exception{
        Playlist playlist = playlistRepository.findByName("summer hits").get(0);
        playlist.setPrivate(false);
        playlistRepository.save(playlist);
        assertFalse(playlistRepository.findByName("summer hits").get(0).isPrivate());
        assertThrows(MakePlaylistPublicException.class, () -> {playlistService.makePlaylistPublic(
                "summer hits");});
    }

}
