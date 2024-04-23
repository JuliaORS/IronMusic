package com.ironhack.controller.impl;

import com.ironhack.controller.interfaces.SongControllerInterface;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.services.interfaces.UserServiceInterface;
import com.ironhack.model.Song;
import com.ironhack.repository.SongRepository;
import com.ironhack.service.impl.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SongController implements SongControllerInterface {


    @Autowired
    private SongService songService;
    @Autowired
    private SongRepository songRepository;
    @Autowired
    private UserServiceInterface userService;

    /*Actions only available to artists-Users*/

    @Override
    @PostMapping("/artist/song")
    @ResponseStatus(HttpStatus.CREATED)
    public Song saveSong(@RequestBody Song song) {
        return songService.saveSong(song);
    }
    @Override
    @DeleteMapping("/users/artist/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSong(@PathVariable Long id) {
        songService.deleteSong(id);
    }


    //TODO: add podcast


    /*Actions available to standard-users*/
    @Override
    @GetMapping("/users/song")
    @ResponseStatus(HttpStatus.OK)
    public List<Song> getAllSongs() {
        return songService.getAllSongs();
    }

    @Override
    @GetMapping("/users/song/{title}")
    @ResponseStatus(HttpStatus.OK)
    public List<Song> getSongByTitle(@PathVariable String title) {
        return songService.getSongByTitle(title);
    }

    @Override
    @GetMapping("/users/song/{artist}")
    @ResponseStatus(HttpStatus.OK)
    public List<Song> getSongByArtist(@PathVariable String artist) {
        return songService.getSongByArtist(artist);
    }

    @Override
    @GetMapping("/users/song/{genre}")
    @ResponseStatus(HttpStatus.OK)
    public List<Song> getSongByGenre(@PathVariable String genre) {
        return songService.getSongByGenre(genre);
    }

}
