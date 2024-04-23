package com.ironhack.controller.interfaces;

import com.ironhack.model.Album;
import com.ironhack.model.Song;
import org.springframework.web.bind.annotation.PathVariable;

public interface AlbumControllerInterface {

    Album saveAlbum(Album album);

    void addSongToAlbum(Long albumId,  Long songId);
    void deleteAlbum(Long id);
}
