package com.ironhack.controller.impl;

import com.ironhack.controller.interfaces.PlaylistControllerInterface;
import com.ironhack.model.Album;
import com.ironhack.model.Playlist;
import com.ironhack.service.impl.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PlaylistController implements PlaylistControllerInterface {
    @Autowired
    PlaylistService playlistService;

    @Override
    @PostMapping("/user/playlist")
    @ResponseStatus(HttpStatus.CREATED)
    public Playlist savePlaylist(@RequestBody Playlist playlist, @AuthenticationPrincipal UserDetails userDetails) {
        return playlistService.savePlaylist(playlist, userDetails);
    }
    @Override
    @PostMapping("/user/playlist/{playlist_id}/audio/{audio_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addAudioToPlaylist(@PathVariable Long playlistId, @PathVariable Long audioId) {
        playlistService.addAudioToPlaylist(playlistId, audioId);
    }


}