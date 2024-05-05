package com.ironhack.controller.impl;

import com.ironhack.controller.interfaces.AlbumControllerInterface;
import com.ironhack.dto.AlbumGeneralInfoDTO;
import com.ironhack.model.Album;
import com.ironhack.service.impl.AlbumService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AlbumController implements AlbumControllerInterface {

    @Autowired
    AlbumService albumService;

    /*Actions only available for artists-Users*/
    @Override
    @PostMapping("/artist/album")
    @ResponseStatus(HttpStatus.CREATED)
    public AlbumGeneralInfoDTO saveAlbum(@Valid @RequestBody Album album) {
        return albumService.saveAlbum(album);
    }

    @Override
    @DeleteMapping("/artist/album/{title}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAlbumByTitle(@PathVariable String title) {
        albumService.deleteAlbumByTitle(title);
    }

    @Override
    @PutMapping("/artist/album/{albumTitle}/song/{songTitle}")
    @ResponseStatus(HttpStatus.OK)
    public void addSongToAlbumByTitleSong(@PathVariable String albumTitle, @PathVariable String songTitle) {
        albumService.addSongToAlbumByTitleSong(albumTitle, songTitle);
    }

    @Override
    @DeleteMapping("/artist/album/{albumTitle}/song/{songTitle}")
    @ResponseStatus(HttpStatus.OK)
    public void removeSongFromAlbum(@PathVariable String albumTitle, @PathVariable String songTitle)  {
        albumService.removeSongFromAlbum(albumTitle, songTitle);
    }

    /*Actions available for standard-users*/
    @Override
    @GetMapping("/user/albums")
    @ResponseStatus(HttpStatus.OK)
    public List<AlbumGeneralInfoDTO> getAllAlbums() {
        return albumService.getAllAlbums();
    }

    @Override
    @GetMapping("/user/album/title/{title}")
    @ResponseStatus(HttpStatus.OK)
    public List<AlbumGeneralInfoDTO> getAlbumByTitle(@PathVariable String title) {
        return albumService.getAlbumByTitle(title);
    }

    @Override
    @GetMapping("/user/album/artist_name/{artistName}")
    @ResponseStatus(HttpStatus.OK)
    public List<AlbumGeneralInfoDTO> getAlbumByArtistName(@PathVariable String artistName) {
        return albumService.getAlbumByArtistName(artistName);
    }

    @Override
    @GetMapping("/user/album/{info}")
    @ResponseStatus(HttpStatus.OK)
    public List<AlbumGeneralInfoDTO> getAlbumByAllInfo(@PathVariable String info) {
        return albumService.getAlbumByAllInfo(info);
    }

}
