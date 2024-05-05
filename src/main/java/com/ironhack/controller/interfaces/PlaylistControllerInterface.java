package com.ironhack.controller.interfaces;

import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.dto.PlaylistGeneralInfoDTO;
import com.ironhack.model.Playlist;

public interface PlaylistControllerInterface {
    PlaylistGeneralInfoDTO savePlaylist(Playlist playlist);
    void deletePlaylistByName(String name);
    AudioGeneralInfoDTO addAudioToPlaylistByTitle(String playlistName, String audioTitle);
    void removeAudioFromPlaylistByTitle(String playlistName, String audioTitle);
    void addUserToPlaylistByUsername(String playlistName, String username);
    void removeUserFromPlaylistByUsername(String playlistName, String username);
}
