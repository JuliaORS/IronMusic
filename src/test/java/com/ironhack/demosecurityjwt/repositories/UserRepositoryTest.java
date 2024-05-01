package com.ironhack.demosecurityjwt.repositories;

import com.ironhack.demosecurityjwt.security.models.Artist;
import com.ironhack.demosecurityjwt.security.models.Role;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.repositories.ArtistRepository;
import com.ironhack.demosecurityjwt.security.repositories.RoleRepository;
import com.ironhack.demosecurityjwt.security.repositories.UserRepository;
import com.ironhack.model.Audio;
import com.ironhack.model.Playlist;
import com.ironhack.model.Song;
import com.ironhack.repository.AudioRepository;
import com.ironhack.repository.PlaylistRepository;
import com.ironhack.repository.SongRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.hibernate.internal.util.collections.CollectionHelper.listOf;
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
        Artist artist = new Artist(new User(null, "artist", "ju", "1234", false, new ArrayList<>(), null));
        artistRepository.save(artist);

        Audio audio = new Audio("tile", "3:34", artist);
        audioRepository.save(audio);
        List<Audio> audiosList = new ArrayList<>();
        audiosList.add(audio);

        Collection<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("ROLE_USER"));

        Playlist playlist = new Playlist("summer hits", audiosList);
        playlistRepository.save(playlist);
        List<Playlist> playlistList = new ArrayList<>();
        playlistList.add(playlist);

        Playlist playlist2 = new Playlist("spring hits", audiosList);
        playlistRepository.save(playlist2);
        List<Playlist> playlistList2 = new ArrayList<>();
        playlistList2.add(playlist2);

        User user = new User(null, "user1", "username1", "1234", false, null, null);
        User user2 = new User(null, "user2", "username2", "1234", true, null, null);

        userRepository.save(user);
        userRepository.save(user2);
        user.setPlaylists(playlistList);
        user.setRoles(roles);
        user2.setPlaylists(playlistList2);
        user2.setRoles(roles);
    }

    @AfterEach
    public void tearsDown(){
        playlistRepository.deleteAll();
        audioRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    public void saveUserTest(){
        long actualResources = userRepository.count();
        User user = new User(null, "userNew", "usernameNew", "1234", false, null, null);
        userRepository.save(user);
        assertEquals(actualResources + 1, userRepository.count());
    }

    @Test
    public void deleteUserTest(){
        long actualResources = userRepository.count();

        User user = new User(null, "userNew", "usernameNew", "1234", false, null, null);
        userRepository.save(user);
        assertEquals(actualResources + 1, userRepository.count());
        userRepository.delete(user);
        assertEquals(actualResources, userRepository.count());
    }

    @Test
    public void deletePlaylistWhenDeleteUserTest(){
        Playlist playlist = new Playlist("90's", null);
        playlistRepository.save(playlist);
        List<Playlist> playlistList = new ArrayList<>();
        playlistList.add(playlist);

        User user = new User(null, "userNew", "usernameNew", "1234", false, null, null);
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
        assertEquals(2, userList.size());
    }
}
