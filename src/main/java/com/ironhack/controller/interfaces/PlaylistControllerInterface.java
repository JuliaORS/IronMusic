package com.ironhack.controller.interfaces;

import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.dto.PlaylistGeneralInfoDTO;
import com.ironhack.model.Playlist;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface PlaylistControllerInterface {
    PlaylistGeneralInfoDTO savePlaylist(Playlist playlist);
    void deletePlaylistByName(String name);
    AudioGeneralInfoDTO addAudioToPlaylistByTitle(String playlistName, String audioTitle);
    void removeAudioFromPlaylistByTitle(String playlistName, String audioTitle);
    void addUserToPlaylistByUsername(String playlistName, String username);
    void removeUserFromPlaylistByUsername(String playlistName, String username);
    List<AudioGeneralInfoDTO> getAllAudiosFromPlaylist(String playlistName);
    List<AudioGeneralInfoDTO> getAllAudiosFromPublicPlaylist(String playlistName);
    void makePlaylistPublic(String playlistName);
}
