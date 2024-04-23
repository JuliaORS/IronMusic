package com.ironhack.controller.interfaces;

import com.ironhack.model.Playlist;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface PlaylistControllerInterface {
    void addAudioToPlaylist(Long playlistId, Long audioId);

    //Playlist savePlaylist(Playlist playlist, UserDetails userDetails);
}
