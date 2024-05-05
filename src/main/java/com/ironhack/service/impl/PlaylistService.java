package com.ironhack.service.impl;

import com.ironhack.demosecurityjwt.security.models.Artist;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.repositories.UserRepository;
import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.dto.PlaylistGeneralInfoDTO;
import com.ironhack.exceptions.ResourceNotFoundException;
import com.ironhack.model.Playlist;
import com.ironhack.model.Audio;
import com.ironhack.repository.AudioRepository;
import com.ironhack.repository.PlaylistRepository;
import com.ironhack.service.interfaces.PlaylistServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaylistService implements PlaylistServiceInterface {

    @Autowired
    private AudioRepository audioRepository;

    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public PlaylistGeneralInfoDTO savePlaylist(@Valid Playlist playlist) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).get();
        user.getPlaylists().add(playlist);
        userRepository.save(user);
        return new PlaylistGeneralInfoDTO(playlist);
    }

    @Override
    public void deletePlaylistByName(String name){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).get();

        Playlist playlistToDelete = user.getPlaylists()
                .stream()
                .filter(playlist -> playlist.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Playlist with name \"" + name + "\" not found"));

        user.getPlaylists().remove(playlistToDelete);
        userRepository.save(user);
        playlistRepository.delete(playlistRepository.findByName(name).get(0));
    }

    @Override
    public AudioGeneralInfoDTO addAudioToPlaylistByTitle(String playlistTitle, String audioTitle){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<Playlist> playlistListOfUserWithSpecificTitle = userRepository.findByUsername(username).get().getPlaylists()
                .stream()
                .filter(playlist -> playlist.getName().equals(playlistTitle))
                .toList();
        if (playlistListOfUserWithSpecificTitle.size() == 1) {
            List<Audio> audioList = audioRepository.findByTitle(audioTitle);
            if (audioList.size() == 1){
                playlistListOfUserWithSpecificTitle.get(0).getAudios().add(audioList.get(0));
                playlistRepository.save(playlistListOfUserWithSpecificTitle.get(0));
                return new AudioGeneralInfoDTO(audioList.get(0));
            } else {
                throw new ResourceNotFoundException("Audio with title \"" + audioTitle + "\" not found");
            }
        } else {
            throw new ResourceNotFoundException("Playlist with title \"" + playlistTitle + "\" not found");
        }
    }

    @Override
    public  void removeAudioFromPlaylistByTitle(String playlistTitle, String audioTitle){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<Playlist> playlistListOfUserWithSpecificTitle = userRepository.findByUsername(username).get().getPlaylists()
                .stream()
                .filter(playlist -> playlist.getName().equals(playlistTitle))
                .toList();
        if (playlistListOfUserWithSpecificTitle.size() == 1) {
            List<Audio> audioList = playlistListOfUserWithSpecificTitle.get(0).getAudios()
                    .stream()
                    .filter(audio -> audio.getTitle().equals(audioTitle))
                    .toList();
            if (audioList.size() == 1){
                playlistListOfUserWithSpecificTitle.get(0).getAudios().remove(audioList.get(0));
                playlistRepository.save(playlistListOfUserWithSpecificTitle.get(0));
            } else {
                throw new ResourceNotFoundException("Audio with title \"" + audioTitle + "\" not found");
            }
        } else {
            throw new ResourceNotFoundException("Playlist with title \"" + playlistTitle + "\" not found");
        }
    }
}
