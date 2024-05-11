package com.ironhack.controller.impl;

import com.ironhack.controller.interfaces.SongControllerInterface;
import com.ironhack.dto.SongGeneralInfoDTO;
import com.ironhack.model.Song;
import com.ironhack.service.impl.SongService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SongController implements SongControllerInterface {

    @Autowired
    private SongService songService;

    /*Actions only available for artists-Users*/
    @Override
    @PostMapping("/artist/song")
    @ResponseStatus(HttpStatus.CREATED)
    public SongGeneralInfoDTO saveSong(@Valid @RequestBody Song song) {
        return songService.saveSong(song);
    }

    @Override
    @DeleteMapping("/artist/song/{title}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSongByTitle(@PathVariable String title) {
        songService.deleteSongByTitle(title);
    }

    /*Actions available for standard-users*/
    @Override
    @GetMapping("/user/songs")
    @ResponseStatus(HttpStatus.OK)
    public List<SongGeneralInfoDTO> getAllSongs() {
        return songService.getAllSongs();
    }

    @Override
    @GetMapping("/user/song/title/{title}")
    @ResponseStatus(HttpStatus.OK)
    public List<SongGeneralInfoDTO> getSongByTitle(@PathVariable String title) {
        return songService.getSongByTitle(title);
    }

    @Override
    @GetMapping("/user/song/artist_name/{artistName}")
    @ResponseStatus(HttpStatus.OK)
    public List<SongGeneralInfoDTO> getSongByArtistName(@PathVariable String artistName) {
        return songService.getSongByArtistName(artistName);
    }

    @Override
    @GetMapping("/user/song/genre/{genre}")
    @ResponseStatus(HttpStatus.OK)
    public List<SongGeneralInfoDTO> getSongByGenre(@PathVariable String genre) {
        return songService.getSongByGenre(genre);
    }

    @Override
    @GetMapping("/user/song/{info}")
    @ResponseStatus(HttpStatus.OK)
    public List<SongGeneralInfoDTO> getSongByAll(@PathVariable String info) {
        return songService.getSongByAllInfo(info);
    }
}
