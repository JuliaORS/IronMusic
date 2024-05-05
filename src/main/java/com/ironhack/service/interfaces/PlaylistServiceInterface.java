package com.ironhack.service.interfaces;

import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.dto.PlaylistGeneralInfoDTO;
import com.ironhack.model.Playlist;

public interface PlaylistServiceInterface {
    PlaylistGeneralInfoDTO savePlaylist(Playlist playlist);

    void deletePlaylistByName(String title);

    AudioGeneralInfoDTO addAudioToPlaylistByTitle(String playlistTitle, String audioTitle);

    void removeAudioFromPlaylistByTitle(String playlistTitle, String audioTitle);

    void addUserToPlaylistByUsername(String playlistName, String username);
    void removeUserFromPlaylistByUsername(String playlistName, String username);

}