package com.ironhack.controller.interfaces;

import com.ironhack.dto.SongGeneralInfoDTO;
import com.ironhack.model.Song;
import java.util.List;

public interface SongControllerInterface {
    SongGeneralInfoDTO saveSong(Song song);
    List<SongGeneralInfoDTO> getAllSongs();
    void deleteSongByTitle(String title);
    List<SongGeneralInfoDTO> getSongByTitle(String title);
    List<SongGeneralInfoDTO> getSongByArtistName(String artist);
    List<SongGeneralInfoDTO> getSongByGenre(String genre);
    public List<SongGeneralInfoDTO> getSongByAll(String info);
}
