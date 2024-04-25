package com.ironhack.service.interfaces;

import com.ironhack.exceptions.ResourceNotFoundException;
import com.ironhack.model.Song;
import com.ironhack.service.impl.SongService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SongServiceInterface {
    Song saveSong(Song song);

    List<Song> getAllSongs();

    void deleteSong(Long id);
    List<Song> getSongByTitle(String title);

    List<Song> getSongByArtist(String artist);

    public List<Song> getSongByGenre(String genre);
}
