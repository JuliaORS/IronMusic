package com.ironhack.controller.interfaces;

import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.dto.PlaylistGeneralInfoDTO;
import com.ironhack.model.Playlist;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

public interface PlaylistControllerInterface {
    PlaylistGeneralInfoDTO savePlaylist(Playlist playlist);
    void deletePlaylistByTitle(@PathVariable String title);
    AudioGeneralInfoDTO addAudioToPlaylistByTitleAudio(String playlistTitle, String audioTitle);
    void removeAudioFromPlaylistByTitle(String playlistTitle, String audioTitle);
}
