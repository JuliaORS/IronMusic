package com.ironhack.service.interfaces;

import com.ironhack.model.Playlist;
import org.springframework.security.core.userdetails.UserDetails;

public interface PlaylistServiceInterface {
    void addAudioToPlaylist(Long playlistId, Long songId);
    public void deletePlaylist(Long id);
    Playlist savePlaylist(Playlist playlist, UserDetails userDetails);

   void removeAudioFromPlaylist(Long playlistId, Long audioId);
}
