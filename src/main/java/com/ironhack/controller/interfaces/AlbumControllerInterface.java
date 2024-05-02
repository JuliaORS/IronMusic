package com.ironhack.controller.interfaces;

import com.ironhack.dto.AlbumGeneralInfoDTO;
import com.ironhack.model.Album;
import com.ironhack.model.Song;
import org.springframework.web.bind.annotation.PathVariable;

public interface AlbumControllerInterface {
    AlbumGeneralInfoDTO saveAlbum(Album album);
    void deleteAlbumByTitle(String title);
    void addSongToAlbum(String albumTitle, String songTitle);
    void removeSongFromAlbum(String albumTitle, String songTitle);
}
