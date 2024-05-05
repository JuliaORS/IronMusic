package com.ironhack.service.interfaces;

import com.ironhack.demosecurityjwt.security.exceptions.UserNotFoundException;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.dto.PlaylistGeneralInfoDTO;
import com.ironhack.exceptions.ResourceNotFoundException;
import com.ironhack.model.Audio;
import com.ironhack.model.Playlist;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface PlaylistServiceInterface {
    PlaylistGeneralInfoDTO savePlaylist(Playlist playlist);

    void deletePlaylistByName(String title);

    AudioGeneralInfoDTO addAudioToPlaylistByTitle(String playlistTitle, String audioTitle);

    void removeAudioFromPlaylistByTitle(String playlistTitle, String audioTitle);

    void addUserToPlaylistByUsername(String playlistName, String username);
    void removeUserFromPlaylistByUsername(String playlistName, String username);

}