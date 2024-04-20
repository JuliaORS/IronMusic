package com.ironhack.service.interfaces;

import com.ironhack.model.Song;
import com.ironhack.service.impl.SongService;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SongServiceInterface {
    Song saveSong(Song song);

    List<Song> getAllSongs();

    void deleteSong(Long id);
}
