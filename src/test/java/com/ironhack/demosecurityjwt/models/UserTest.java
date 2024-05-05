package com.ironhack.demosecurityjwt.models;

import com.ironhack.demosecurityjwt.security.Utils.ArtistStatus;
import com.ironhack.demosecurityjwt.security.models.Artist;
import com.ironhack.demosecurityjwt.security.models.Role;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.model.Album;
import com.ironhack.model.Audio;
import com.ironhack.model.Playlist;
import com.ironhack.model.Song;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserTest {

    @Test
    public void userConstructorVoidTest(){
        User user = new User();
        assertNotNull(user);
    }

    @Test
    public void userConstructorTest(){
        User user = new User(null, "Julia", "ju", "1234",
                true, ArtistStatus.INACTIVE,  new ArrayList<>(), null);
        assertNotNull(user);
    }

    @Test
    public void userSetterGetterTest(){
        User user = new User(null, "name", "co", "1234",
                true, ArtistStatus.ACTIVE, new ArrayList<>(), null);

        Role roleUser = new Role("ROLE_USER");
        Collection<Role> roles = new ArrayList<>();
        roles.add(roleUser);

        Artist artist = new Artist(new User(null, "artist", "ju", "1234",
                true, ArtistStatus.ACTIVE, new ArrayList<>(), null));
        Audio audio = new Audio("tile", "3:34", artist);
        List<Audio> audiosList = new ArrayList<>();
        audiosList.add(audio);

        Playlist playlist = new Playlist("summer hits", audiosList);
        List<Playlist> playlistList = new ArrayList<>();
        playlistList.add(playlist);

        user.setName("julia");
        user.setRoles(roles);
        //user.setPlaylists(playlistList);

        assertEquals("julia", user.getName());
        assertEquals(1, user.getRoles().size());
       // assertEquals(1, user.getPlaylists().size());
    }
}
