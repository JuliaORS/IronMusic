package com.ironhack.service.interfaces;

import com.ironhack.dto.AlbumGeneralInfoDTO;
import com.ironhack.exceptions.ResourceNotFoundException;
import com.ironhack.model.Album;
import com.ironhack.model.Song;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

public interface AlbumServiceInterface {
    AlbumGeneralInfoDTO saveAlbum(Album album);
    void deleteAlbumByTitle(String title);
    void addSongToAlbumByTitleSong(String albumTitle, String songTitle);
    void removeSongFromAlbum(String albumTitle, String songTitle);
    List<AlbumGeneralInfoDTO> getAllAlbums();
    List<AlbumGeneralInfoDTO> getAlbumByTitle(String title);
    List<AlbumGeneralInfoDTO> getAlbumByArtistName(String artist);
    List<AlbumGeneralInfoDTO> getAlbumByAllInfo(String info);
}
