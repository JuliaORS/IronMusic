package com.ironhack.controller.interfaces;

import com.ironhack.dto.AlbumGeneralInfoDTO;
import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.model.Album;
import com.ironhack.model.Song;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

public interface AlbumControllerInterface {
    AlbumGeneralInfoDTO saveAlbum(Album album);
    void deleteAlbumByTitle(String title);
    void addSongToAlbumByTitleSong(String albumTitle, String songTitle);
    void removeSongFromAlbum(String albumTitle, String songTitle);
    List<AlbumGeneralInfoDTO> getAllAlbums();
    List<AlbumGeneralInfoDTO> getAlbumByTitle(String title);
    List<AlbumGeneralInfoDTO> getSongByArtistName(String artistName);
    List<AlbumGeneralInfoDTO> getAlbumByAll(String info);
}
