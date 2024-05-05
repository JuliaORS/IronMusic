package com.ironhack.controller.impl;

import com.ironhack.controller.interfaces.PlaylistControllerInterface;
import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.dto.PlaylistGeneralInfoDTO;
import com.ironhack.model.Playlist;
import com.ironhack.service.impl.PlaylistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Override
    @PutMapping("/user/playlist/{playlistName}/user/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void addUserToPlaylistByUsername(@PathVariable String playlistName,
                                                         @PathVariable String username) {
        playlistService.addUserToPlaylistByUsername(playlistName, username);
    }

    @Override
    @DeleteMapping("/user/playlist/{playlistName}/user/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void removeUserFromPlaylistByUsername(@PathVariable String playlistName, @PathVariable String username)  {
        playlistService.removeUserFromPlaylistByUsername(playlistName, username);
    }
}
