package com.ironhack.service.impl;

import com.ironhack.model.Song;
import com.ironhack.security.exception.UserNotFoundException;
import com.ironhack.security.model.User;
import com.ironhack.security.repository.UserRepository;
import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.dto.PlaylistGeneralInfoDTO;
import com.ironhack.exception.ResourceNotFoundException;
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
    public void deletePlaylistByName(String name) {
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
    public AudioGeneralInfoDTO addAudioToPlaylistByTitle(String playlistName, String audioTitle) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Playlist playlistUserWithSpecificName = userRepository.findByUsername(username).get().getPlaylists()
                .stream()
                .filter(playlist -> playlist.getName().equals(playlistName))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Playlist with name \"" + playlistName + "\" not found"));

        Audio audio = audioRepository.findByTitle(audioTitle)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Audio with title \"" + audioTitle + "\" not found"));

        playlistUserWithSpecificName.getAudios().add(audio);
        playlistRepository.save(playlistUserWithSpecificName);
        return new AudioGeneralInfoDTO(audio);
    }

    @Override
    public void removeAudioFromPlaylistByTitle(String playlistName, String audioTitle) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Playlist playlistUserWithSpecificName = userRepository.findByUsername(username).get().getPlaylists()
                .stream()
                .filter(playlist -> playlist.getName().equals(playlistName))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Playlist with name \"" + playlistName + "\" not found"));

        Audio audio = playlistUserWithSpecificName.getAudios()
                .stream()
                .filter(a -> a.getTitle().equals(audioTitle))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Audio with title \"" + audioTitle + "\" not found"));

        playlistUserWithSpecificName.getAudios().remove(audio);
        playlistRepository.save(playlistUserWithSpecificName);
    }

    @Override
    public void addUserToPlaylistByUsername(String playlistName, String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usernameUserAction = authentication.getName();

        Playlist playlistUserWithSpecificName = userRepository.findByUsername(usernameUserAction).get()
                .getPlaylists()
                .stream()
                .filter(playlist -> playlist.getName().equals(playlistName))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Playlist with name \"" + playlistName + "\" not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username \"" + username + "\" not found"));

        user.getPlaylists().add(playlistUserWithSpecificName);
        userRepository.save(user);
    }

    @Override
    public void removeUserFromPlaylistByUsername(String playlistName, String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usernameUserAction = authentication.getName();
        User user = userRepository.findByUsername(usernameUserAction).get();

        Playlist playlist = user.getPlaylists()
                .stream()
                .filter(p -> p.getName().equals(playlistName))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Playlist with name \"" + playlistName + "\" not found"));

        User userToRemove = playlist.getUsers()
                .stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("User with username \"" + username + "\" not found"));

        userToRemove.getPlaylists().remove(playlist);
        userRepository.save(userToRemove);
    }
    @Override
    public List<AudioGeneralInfoDTO> getAllAudiosFromPlaylist(String playlistName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).get();

        Playlist playlist = user.getPlaylists()
                .stream()
                .filter(p -> p.getName().equals(playlistName))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Playlist with name \"" + playlistName + "\" not found"));
        if (playlist.getAudios().isEmpty()){
            throw new ResourceNotFoundException("Playlist with name \"" + playlistName + "\" is empty");
        }
        List<AudioGeneralInfoDTO> result = new ArrayList<>();
        for(Audio audio : playlist.getAudios()){
            result.add(new AudioGeneralInfoDTO(audio));
        }
        return result;
    }
}