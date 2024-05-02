package com.ironhack.controller.impl;

import com.ironhack.controller.interfaces.AlbumControllerInterface;
import com.ironhack.dto.AlbumGeneralInfoDTO;
import com.ironhack.model.Album;
import com.ironhack.model.Song;
import com.ironhack.service.impl.AlbumService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AlbumController implements AlbumControllerInterface {

    @Autowired
    AlbumService albumService;
    @Override
    @PostMapping("/artist/album")
    @ResponseStatus(HttpStatus.CREATED)
    public AlbumGeneralInfoDTO saveAlbum(@Valid @RequestBody Album album) {
        return albumService.saveAlbum(album);
    }

    @Override
    @DeleteMapping("/artist/album/{title}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAlbumByTitle(@PathVariable String title) {
        albumService.deleteAlbumByTitle(title);
    }

    @Override
    @PostMapping("/artist/album/{album_title}/song/{song_title}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addSongToAlbumByTitleSong(@PathVariable String albumTitle, @PathVariable String songTitle) {
        albumService.addSongToAlbumByTitleSong(albumTitle, songTitle);
    }

    @Override
    @DeleteMapping("/artist/album/{album_title}/song/{song_title}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeSongFromAlbum(@PathVariable String albumTitle, @PathVariable String songTitle)  {
        albumService.removeSongFromAlbum(albumTitle, songTitle);
    }

}
