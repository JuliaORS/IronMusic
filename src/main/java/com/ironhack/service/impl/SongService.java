package com.ironhack.service.impl;

import com.ironhack.utils.DurationAudioFormatValidator;
import com.ironhack.security.model.Artist;
import com.ironhack.security.model.User;
import com.ironhack.security.repository.UserRepository;
import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.exception.ResourceNotFoundException;
import com.ironhack.model.Song;
import com.ironhack.repository.SongRepository;
import com.ironhack.service.interfaces.SongServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class SongService  implements SongServiceInterface {

    @Autowired
    private SongRepository songRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public AudioGeneralInfoDTO saveSong(@Valid Song song) {
        new DurationAudioFormatValidator(song.getDuration());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).get();
        song.setArtist((Artist) user);
        songRepository.save(song);
        return new AudioGeneralInfoDTO(song);
    }

    @Override
    public void deleteSongByTitle(String title){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Song song = songRepository.findByTitleAndArtistUsername(title, username)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Song with title \"" + title + "\" not found"));

        songRepository.delete(song);
    }

    @Override
    public List<AudioGeneralInfoDTO> getAllSongs() {
        List<AudioGeneralInfoDTO> result = new ArrayList<>();
        List<Song> songs = songRepository.findAll();
        for(Song song : songs){
            result.add(new AudioGeneralInfoDTO(song));
        }
        return result;
    }


    @Override
    public List<AudioGeneralInfoDTO> getSongByTitle(String title) {
        List<Song> songs = songRepository.findByTitleContaining(title);
        List<AudioGeneralInfoDTO> result = new ArrayList<>();
        if (songs.isEmpty()){
            throw new ResourceNotFoundException("No songs found with that title.");
        } else {
            for(Song song : songs){
                result.add(new AudioGeneralInfoDTO(song));
            }
        }
        return result;
    }

    @Override
    public List<AudioGeneralInfoDTO> getSongByArtistName(String artist) {
        List<Song> songs = songRepository.findByArtistNameContaining(artist);
        List<AudioGeneralInfoDTO> result = new ArrayList<>();
        if (songs.isEmpty()){
            throw new ResourceNotFoundException("No songs found with that artist name.");
        } else {
            for(Song song : songs){
                result.add(new AudioGeneralInfoDTO(song));
            }
        }
        return result;
    }

    @Override
    public List<AudioGeneralInfoDTO> getSongByGenre(String genre) {
        List<Song> songs = songRepository.findByGenreContaining(genre);
        List<AudioGeneralInfoDTO> result = new ArrayList<>();
        if (songs.isEmpty()){
            throw new ResourceNotFoundException("No songs found with that genre.");
        } else {
            for(Song song : songs){
                result.add(new AudioGeneralInfoDTO(song));
            }
        }
        return result;
    }

    @Override
    public List<AudioGeneralInfoDTO> getSongByAllInfo(String info) {
        List<Song> songs = songRepository.findByArtistNameContainingOrTitleContainingOrAlbumTitleContaining(info, info, info);
        List<AudioGeneralInfoDTO> result = new ArrayList<>();
        if (songs.isEmpty()){
            throw new ResourceNotFoundException("No songs found with that info.");
        } else {
            for(Song song : songs){
                result.add(new AudioGeneralInfoDTO(song));
            }
        }
        return result;
    }
}
