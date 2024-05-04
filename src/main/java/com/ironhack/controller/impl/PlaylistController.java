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
    @PostMapping("/artist/playlist")
    @ResponseStatus(HttpStatus.CREATED)
    public PlaylistGeneralInfoDTO savePlaylist(@Valid @RequestBody Playlist playlist) {
        return playlistService.savePlaylist(playlist);
    }

    @Override
    @DeleteMapping("/artist/playlist/{title}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePlaylistByTitle(@PathVariable String title) {
        playlistService.deletePlaylistByTitle(title);
    }

    @Override
    @PutMapping("/artist/playlist/{playlistTitle}/audio/{audioTitle}")
    @ResponseStatus(HttpStatus.OK)
    public AudioGeneralInfoDTO addAudioToPlaylistByTitleAudio(@PathVariable String playlistTitle,
                                                              @PathVariable String audioTitle) {
        return playlistService.addAudioToPlaylistByTitleAudio(playlistTitle, audioTitle);
    }

    @Override
    @DeleteMapping("/artist/playlist/{playlistTitle}/audio/{audioTitle}")
    @ResponseStatus(HttpStatus.OK)
    public void removeAudioFromPlaylistByTitle(@PathVariable String playlistTitle, @PathVariable String audioTitle)  {
        playlistService.removeAudioFromPlaylistByTitle(playlistTitle, audioTitle);
    }
}
