package com.ironhack.service.interfaces;

import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.exceptions.ResourceNotFoundException;
import com.ironhack.model.Song;
import com.ironhack.service.impl.SongService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SongServiceInterface {
    AudioGeneralInfoDTO saveSong(Song song);

    List<AudioGeneralInfoDTO> getAllSongs();

    void deleteSong(Long id);
    List<AudioGeneralInfoDTO> getSongByTitle(String title);

    List<AudioGeneralInfoDTO> getSongByArtistName(String artist);

    public List<AudioGeneralInfoDTO> getSongByGenre(String genre);
}
