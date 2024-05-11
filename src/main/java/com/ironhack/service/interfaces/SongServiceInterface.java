package com.ironhack.service.interfaces;

import com.ironhack.dto.SongGeneralInfoDTO;
import com.ironhack.model.Song;

import java.util.List;

public interface SongServiceInterface {
    SongGeneralInfoDTO saveSong(Song song);

    List<SongGeneralInfoDTO> getAllSongs();

    void deleteSongByTitle(String title);
    List<SongGeneralInfoDTO> getSongByTitle(String title);

    List<SongGeneralInfoDTO> getSongByArtistName(String artist);

    List<SongGeneralInfoDTO> getSongByGenre(String genre);
    List<SongGeneralInfoDTO> getSongByAllInfo(String info);
}
