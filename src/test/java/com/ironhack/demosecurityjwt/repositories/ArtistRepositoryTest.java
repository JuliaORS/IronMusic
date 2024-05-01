package com.ironhack.demosecurityjwt.repositories;

import com.ironhack.demosecurityjwt.security.models.Artist;
import com.ironhack.demosecurityjwt.security.models.Role;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.repositories.ArtistRepository;
import com.ironhack.demosecurityjwt.security.repositories.RoleRepository;
import com.ironhack.demosecurityjwt.security.repositories.UserRepository;
import com.ironhack.model.Album;
import com.ironhack.model.Audio;
import com.ironhack.model.Playlist;
import com.ironhack.model.Song;
import com.ironhack.repository.AlbumRepository;
import com.ironhack.repository.AudioRepository;
import com.ironhack.repository.PlaylistRepository;
import com.ironhack.repository.SongRepository;
import jakarta.validation.constraints.AssertFalse;
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
public class ArtistRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AudioRepository audioRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @BeforeEach
    public void setUp(){
        Artist artist = new Artist(new User(null, "artist", "ju", "1234", false, new ArrayList<>(), null));
        Artist artistSaved = artistRepository.save(artist);

        Song song = new Song("title1", "3:34", artistSaved, null, "pop");
        Audio audio = new Audio("title2", "3:24", artistSaved);
        songRepository.save(song);
        audioRepository.save(audio);

        Album album = new Album("album", artistSaved, null);

        albumRepository.save(album);

        song.setAlbum(album);
        songRepository.save(song);

        List<Album> albumList = new ArrayList<>();
        albumList.add(album);
        artistSaved.setAlbums(albumList);

        Collection<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("ROLE_USER"));
        roles.add(roleRepository.findByName("ROLE_ARTIST"));
        artistSaved.setRoles(roles);
        List<Audio> audiosList = new ArrayList<>();
        audiosList.add(audio);
        artistSaved.setAudios(audiosList);
    }

    @AfterEach
    public void tearsDown(){
        albumRepository.deleteAll();
        audioRepository.deleteAll();
        songRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    public void saveArtistTest(){
        long initialResources = userRepository.count();
        Artist artist = new Artist(new User(null, "userNew", "usernameNew", "1234", false, null, null));
        userRepository.save(artist);
        assertEquals(initialResources + 1, userRepository.count());
    }

    @Test
    public void deleteArtistTest(){
        long actualResources = userRepository.count();
        Artist artist = new Artist(new User(null, "userNew", "usernameNew", "1234", false, null, null));
        userRepository.save(artist);
        assertEquals(actualResources + 1, userRepository.count());
        userRepository.delete(artist);
        assertEquals(actualResources, userRepository.count());
    }

    @Test
    public void deleteAlbumAndSongsWhenDeleteArtistTest(){
        Optional<Artist> optionalArtist = artistRepository.findByUsername("ju");
        if (optionalArtist.isPresent()){
            Artist artist = optionalArtist.get();
            assertEquals(2, audioRepository.count());
            assertEquals(1, albumRepository.count());
            artistRepository.delete(artist);
            assertEquals(0, audioRepository.count());
            assertEquals(0, albumRepository.count());
        }
    }
}