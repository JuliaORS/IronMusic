package com.ironhack.service.impl;

import com.ironhack.Exceptions.ResourceNotFoundException;
import com.ironhack.model.Song;
import com.ironhack.repository.SongRepository;
import com.ironhack.service.interfaces.SongServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SongService  implements SongServiceInterface {

    @Autowired
    private SongRepository songRepository;
    @Override
    public Song saveSong(Song song) {
        return songRepository.save(song);
    }

    @Override
    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    @Override
    public void deleteSong(Long id){
        Optional<Song> songOptional = songRepository.findById(id);
        if (songOptional.isPresent()){
            songRepository.delete(songOptional.get());
        } else {
            throw new ResourceNotFoundException("Song with ID " + id + " not found");
        }
    }
}
