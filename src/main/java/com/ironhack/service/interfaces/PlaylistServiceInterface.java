package com.ironhack.service.interfaces;

import com.ironhack.model.Playlist;
import org.springframework.security.core.userdetails.UserDetails;

public interface PlaylistServiceInterface {
    void addAudioToPlaylist(Long playlistId, Long songId);
    Playlist savePlaylist(Playlist playlist, UserDetails userDetails);
}
