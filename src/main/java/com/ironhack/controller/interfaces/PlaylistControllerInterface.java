package com.ironhack.controller.interfaces;

import com.ironhack.model.Playlist;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface PlaylistControllerInterface {
    Playlist savePlaylist(Playlist playlist, UserDetails userDetails);
    void deletePlaylistByTitle(String title);
    void addAudioToPlaylist(Long playlistId, Long audioId);
    void removeAudioFromPlaylist(Long playlistId, Long audioId);
}
