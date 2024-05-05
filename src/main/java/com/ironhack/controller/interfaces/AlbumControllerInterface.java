package com.ironhack.controller.interfaces;

import com.ironhack.dto.AlbumGeneralInfoDTO;
import com.ironhack.model.Album;
import java.util.List;

public interface AlbumControllerInterface {
    AlbumGeneralInfoDTO saveAlbum(Album album);
    void deleteAlbumByTitle(String title);
    void addSongToAlbumByTitleSong(String albumTitle, String songTitle);
    void removeSongFromAlbum(String albumTitle, String songTitle);
    List<AlbumGeneralInfoDTO> getAllAlbums();
    List<AlbumGeneralInfoDTO> getAlbumByTitle(String title);
    List<AlbumGeneralInfoDTO> getAlbumByArtistName(String artistName);
    List<AlbumGeneralInfoDTO> getAlbumByAllInfo(String info);
}
