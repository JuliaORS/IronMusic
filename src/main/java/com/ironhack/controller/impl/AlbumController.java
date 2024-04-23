package com.ironhack.controller.impl;

import com.ironhack.controller.interfaces.AlbumControllerInterface;
import com.ironhack.model.Album;
import com.ironhack.model.Song;
import com.ironhack.service.impl.AlbumService;
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
    public Album saveAlbum(@RequestBody Album album) {
        return albumService.saveAlbum(album);
    }

    @Override
    @PostMapping("/artist/album/{album_id}/song/{song_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addSongToAlbum(@PathVariable Long albumId, @PathVariable Long songId) {
        albumService.addSongToAlbum(albumId, songId);
    }

    @Override
    @DeleteMapping("/artist/album")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAlbum(@PathVariable Long id) {
        albumService.deleteAlbum(id);
    }

}
