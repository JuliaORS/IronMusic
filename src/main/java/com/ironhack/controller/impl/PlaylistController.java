package com.ironhack.controller.impl;

import com.ironhack.controller.interfaces.PlaylistControllerInterface;
import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.dto.PlaylistGeneralInfoDTO;
import com.ironhack.model.Playlist;
import com.ironhack.model.Playlist;
import com.ironhack.service.impl.PlaylistService;
import jakarta.validation.Valid;
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
    public PlaylistGeneralInfoDTO savePlaylist(@Valid @RequestBody Playlist playlist) {
        return playlistService.savePlaylist(playlist);
    }

    @Override
    @DeleteMapping("/user/playlist/{name}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePlaylistByName(@PathVariable String name) {
        playlistService.deletePlaylistByName(name);
    }

    @Override
    @PutMapping("/user/playlist/{playlistName}/audio/{audioTitle}")
    @ResponseStatus(HttpStatus.OK)
    public AudioGeneralInfoDTO addAudioToPlaylistByTitle(@PathVariable String playlistName,
                                                              @PathVariable String audioTitle) {
        return playlistService.addAudioToPlaylistByTitle(playlistName, audioTitle);
    }

    @Override
    @DeleteMapping("/user/playlist/{playlistName}/audio/{audioTitle}")
    @ResponseStatus(HttpStatus.OK)
    public void removeAudioFromPlaylistByTitle(@PathVariable String playlistName, @PathVariable String audioTitle)  {
        playlistService.removeAudioFromPlaylistByTitle(playlistName, audioTitle);
    }
}
