package com.ironhack.controller.interfaces;

import com.ironhack.dto.AlbumGeneralInfoDTO;
import com.ironhack.model.Album;
import com.ironhack.model.Song;
import org.springframework.web.bind.annotation.PathVariable;

public interface AlbumControllerInterface {
    AlbumGeneralInfoDTO saveAlbum(Album album);
    void deleteAlbum(Long id);
    void addSongToAlbum(Long albumId, Long songId);
    void removeSongFromAlbum(Long albumId, Long songId);
}
