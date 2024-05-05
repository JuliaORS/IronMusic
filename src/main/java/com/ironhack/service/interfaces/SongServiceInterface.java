package com.ironhack.service.interfaces;

import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.model.Song;

import java.util.List;

public interface SongServiceInterface {
    AudioGeneralInfoDTO saveSong(Song song);

    List<AudioGeneralInfoDTO> getAllSongs();

    void deleteSongByTitle(String title);
    List<AudioGeneralInfoDTO> getSongByTitle(String title);

    List<AudioGeneralInfoDTO> getSongByArtistName(String artist);

    List<AudioGeneralInfoDTO> getSongByGenre(String genre);
    List<AudioGeneralInfoDTO> getSongByAllInfo(String info);
}
