package com.ironhack.controller.interfaces;

import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.model.Song;
import java.util.List;

public interface SongControllerInterface {
    AudioGeneralInfoDTO saveSong(Song song);
    List<AudioGeneralInfoDTO> getAllSongs();
    void deleteSongByTitle(String title);
    List<AudioGeneralInfoDTO> getSongByTitle(String title);
    List<AudioGeneralInfoDTO> getSongByArtistName(String artist);
    List<AudioGeneralInfoDTO> getSongByGenre(String genre);
    public List<AudioGeneralInfoDTO> getSongByAll(String info);
}
