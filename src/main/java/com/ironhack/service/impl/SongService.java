package com.ironhack.service.impl;

import com.ironhack.Utils.Validator;
import com.ironhack.demosecurityjwt.security.models.Artist;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.repositories.UserRepository;
import com.ironhack.exceptions.BadRequestFormatException;
import com.ironhack.exceptions.ResourceNotFoundException;
import com.ironhack.model.Playlist;
import com.ironhack.model.Song;
import com.ironhack.repository.SongRepository;
import com.ironhack.service.interfaces.SongServiceInterface;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import java.util.List;
import java.util.Optional;

@Service
public class SongService  implements SongServiceInterface {

    @Autowired
    private SongRepository songRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public Song saveSong(@Valid Song song) {
        if (!Validator.durationAudioValidator(song.getDuration())) {
            throw new BadRequestFormatException("Bad request. Duration has not a correct format: HH:MM:SS or MM:SS or SS");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        song.setArtist((Artist) user);
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
