package com.ironhack.service.interfaces;

import com.ironhack.dto.AlbumGeneralInfoDTO;
import com.ironhack.model.Album;
import com.ironhack.model.Song;
import org.springframework.web.bind.annotation.RequestBody;

public interface AlbumServiceInterface {
    AlbumGeneralInfoDTO saveAlbum(Album album);
    void deleteAlbumByTitle(String title);
    void addSongToAlbumByTitleSong(String albumTitle, String songTitle);
    void removeSongFromAlbum(String albumTitle, String songTitle);
}
