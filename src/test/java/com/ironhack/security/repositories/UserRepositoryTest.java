package com.ironhack.security.repositories;

import com.ironhack.security.utils.ArtistStatus;
import com.ironhack.security.model.Artist;
import com.ironhack.security.model.Role;
import com.ironhack.security.model.User;
import com.ironhack.security.repository.ArtistRepository;
import com.ironhack.security.repository.RoleRepository;
import com.ironhack.security.repository.UserRepository;
import com.ironhack.model.Audio;
import com.ironhack.model.Playlist;
import com.ironhack.repository.AudioRepository;
import com.ironhack.repository.PlaylistRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AudioRepository audioRepository;

    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private RoleRepository roleRepository;


    @BeforeEach
    public void setUp(){
        Artist artist = new Artist(new User(null, "artist", "ju", "1234",
                true, ArtistStatus.ACTIVE,  new ArrayList<>(), null));
        artistRepository.save(artist);

        Audio audio = new Audio("tile", "3:34", artist);
        audioRepository.save(audio);
        List<Audio> audiosList = new ArrayList<>(List.of(audio));

        Collection<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("ROLE_USER").get());

        User user = new User(null, "user1", "username1", "1234",
                true, ArtistStatus.INACTIVE, roles, null);
        userRepository.save(user);
        List<User> userList = new ArrayList<>(List.of(user));

        Playlist playlist = new Playlist("summer hits", null, userList);
        playlistRepository.save(playlist);

        List<Playlist> playlistList = new ArrayList<>(List.of(playlist));
        user.setPlaylists(playlistList);
        userRepository.save(user);
    }

    @AfterEach
    public void tearsDown(){
        userRepository.deleteAll();
        artistRepository.deleteAll();
        audioRepository.deleteAll();
        playlistRepository.deleteAll();
    }

    @Test
    public void saveUserTest(){
        long actualResources = userRepository.count();
        User user = new User(null, "userNew", "usernameNew", "1234",
                true, ArtistStatus.INACTIVE, null, null);
        userRepository.save(user);
        assertEquals(actualResources + 1, userRepository.count());
    }

    @Test
    public void deleteUserTest(){
        long actualResources = userRepository.count();
        userRepository.delete(userRepository.findByUsername("username1").get());
        assertEquals(actualResources - 1, userRepository.count());
    }

    @Test
    public void deletePlaylistWhenDeleteUserTest(){
        Playlist playlist = new Playlist("90's", null, null);
        playlistRepository.save(playlist);
        List<Playlist> playlistList = new ArrayList<>();
        playlistList.add(playlist);

        User user = new User(null, "userNew", "usernameNew", "1234",
                true, ArtistStatus.INACTIVE, null, null);
        userRepository.save(user);
        user.setPlaylists(playlistList);

        assertFalse(playlistRepository.findByName("90's").isEmpty());
        userRepository.delete(user);
        assertTrue(playlistRepository.findByName("90's").isEmpty());
    }

    @Test
    public void findByUsernameTest(){
        Optional<User> optionalUser = userRepository.findByUsername("username1");
        assertTrue(optionalUser.isPresent());
    }

    @Test
    public void findByUsernameNotExistingNameTest(){
        Optional<User> optionalUser = userRepository.findByUsername("no name");
        assertTrue(optionalUser.isEmpty());
    }

    @Test
    public void findByIsActiveFalseTest(){
        List<User> userList = userRepository.findByIsActiveFalse();
        for (User user : userList){
            assertTrue(user.isActive());
        }
    }

    @Test
    public void findByArtistStatusTest(){
        List<User> userList = userRepository.findByArtistStatus(ArtistStatus.ACTIVE);
        for (User user : userList){
            assertEquals(ArtistStatus.ACTIVE, user.getArtistStatus());
        }
    }
}
