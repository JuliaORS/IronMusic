package com.ironhack.controller.impl;

import com.ironhack.controller.interfaces.SongControllerInterface;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.services.interfaces.UserServiceInterface;
import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.model.Song;
import com.ironhack.repository.SongRepository;
import com.ironhack.service.impl.SongService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
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
    public AudioGeneralInfoDTO saveSong(@Valid @RequestBody Song song) {
        return songService.saveSong(song);
    }

    @Override
    @DeleteMapping("/artist/song/{title}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSongByTitle(@PathVariable String title) {
        songService.deleteSongByTitle(title);
    }

    /*Actions available to standard-users*/
    @Override
    @GetMapping("/users/songs")
    @ResponseStatus(HttpStatus.OK)
    public List<AudioGeneralInfoDTO> getAllSongs() {
        return songService.getAllSongs();
    }

    @Override
    @GetMapping("/users/song/title/{title}")
    @ResponseStatus(HttpStatus.OK)
    public List<AudioGeneralInfoDTO> getSongByTitle(@PathVariable String title) {
        return songService.getSongByTitle(title);
    }

    @Override
    @GetMapping("/users/song/artist_name/{artistName}")
    @ResponseStatus(HttpStatus.OK)
    public List<AudioGeneralInfoDTO> getSongByArtistName(@PathVariable String artistName) {
        return songService.getSongByArtistName(artistName);
    }

    @Override
    @GetMapping("/users/song/genre/{genre}")
    @ResponseStatus(HttpStatus.OK)
    public List<AudioGeneralInfoDTO> getSongByGenre(@PathVariable String genre) {
        return songService.getSongByGenre(genre);
    }

    @Override
    @GetMapping("/users/song/{info}")
    @ResponseStatus(HttpStatus.OK)
    public List<AudioGeneralInfoDTO> getSongByAll(@PathVariable String info) {
        return songService.getSongByAllInfo(info);
    }
}
