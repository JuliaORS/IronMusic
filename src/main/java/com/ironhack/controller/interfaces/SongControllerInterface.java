package com.ironhack.controller.interfaces;

import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.model.Song;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface SongControllerInterface {
    AudioGeneralInfoDTO saveSong(Song song);
    List<Song> getAllSongs();
    void deleteSong(Long id);

    List<Song> getSongByTitle(String title);
    List<Song> getSongByArtistName(String artist);
    List<Song> getSongByGenre(String genre);
}
