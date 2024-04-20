package com.ironhack.controller.interfaces;

import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.model.Song;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface SongControllerInterface {
    Song saveSong(Song song);
    List<Song> getAllSongs();
    void deleteSong(Long id);
}
