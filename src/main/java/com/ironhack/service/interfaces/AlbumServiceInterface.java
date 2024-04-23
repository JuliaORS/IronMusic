package com.ironhack.service.interfaces;

import com.ironhack.model.Album;
import com.ironhack.model.Song;
import org.springframework.web.bind.annotation.RequestBody;

public interface AlbumServiceInterface {
    Album saveAlbum(Album album);
    void addSongToAlbum(Long albumId, Long songId);
    void deleteAlbum(Long id);
}
