package com.ironhack.service.impl;

import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.exceptions.ResourceNotFoundException;
import com.ironhack.model.Audio;
import com.ironhack.model.Playlist;
import com.ironhack.repository.AudioRepository;
import com.ironhack.repository.PlaylistRepository;
import com.ironhack.service.interfaces.PlaylistServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlaylistService implements PlaylistServiceInterface {

    @Autowired
    private AudioRepository audioRepository;

    @Autowired
    private PlaylistRepository playlistRepository;
    @Override
    public Playlist savePlaylist(Playlist playlist, UserDetails userDetails){
        User user = (User) userDetails;
        Playlist playlistSaved = playlistRepository.save(playlist);
        user.getPlaylists().add(playlistSaved);
        return playlistSaved;
    }

    @Override
    public void addAudioToPlaylist(Long playlistId, Long audioId){
        Optional<Playlist> playlistOptional = playlistRepository.findById(playlistId);
        if (playlistOptional.isPresent()){
            Optional<Audio> audioOptional = audioRepository.findById(audioId);
            if (audioOptional.isPresent()) {
                Playlist playlist = playlistOptional.get();
                playlist.getAudios().add(audioOptional.get());
            } else {
                throw new ResourceNotFoundException("Audio with ID " + audioId + " not found");
            }
        } else {
            throw new ResourceNotFoundException("Playlist with ID " + playlistId + " not found");
        }
    }

    @Override
    public void removeAudioFromPlaylist(Long playlistId, Long audioId){
        Optional<Playlist> playlistOptional = playlistRepository.findById(playlistId);
        if (playlistOptional.isPresent()){
            Playlist playlist = playlistOptional.get();
            boolean removed = playlist.getAudios().removeIf(audio -> audio.getId().equals(audioId));
            if (!removed) {
                throw new ResourceNotFoundException("Audio with ID " + audioId + " not found in playlist");
            }
        } else {
            throw new ResourceNotFoundException("Playlist with ID " + playlistId + " not found");
        }
    }
}
