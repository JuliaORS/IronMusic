package com.ironhack.service.impl;

import com.ironhack.exceptions.ResourceNotFoundException;
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
    @Override
    public List<Song> getSongByTitle(String title) {
        List<Song> songs = songRepository.findByTitleContaining(title);
        if (songs.isEmpty()){
            throw new ResourceNotFoundException("No songs found with that title.");
        } else {
            return songs;
        }
    }

    @Override
    public List<Song> getSongByArtist(String artist) {
        List<Song> songs = songRepository.findByArtistContaining(artist);
        if (songs.isEmpty()){
            throw new ResourceNotFoundException("No songs found of that artist.");
        } else {
            return songs;
        }
    }

    @Override
    public List<Song> getSongByGenre(String genre) {
        List<Song> songs = songRepository.findByGenreContaining(genre);
        if (songs.isEmpty()){
            throw new ResourceNotFoundException("No songs found of that genre.");
        } else {
            return songs;
        }
    }


}
