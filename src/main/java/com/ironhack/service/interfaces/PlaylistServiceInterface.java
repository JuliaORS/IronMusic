package com.ironhack.service.interfaces;

import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.dto.PlaylistGeneralInfoDTO;
import com.ironhack.model.Playlist;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface PlaylistServiceInterface {
    PlaylistGeneralInfoDTO savePlaylist(Playlist playlist);

    void deletePlaylistByName(String title);

    AudioGeneralInfoDTO addAudioToPlaylistByTitle(String playlistTitle, String audioTitle);

    void removeAudioFromPlaylistByTitle(String playlistTitle, String audioTitle);

    void addUserToPlaylistByUsername(String playlistName, String username);
    void removeUserFromPlaylistByUsername(String playlistName, String username);
    List<AudioGeneralInfoDTO> getAllAudiosFromPlaylist(String playlistName);
    List<AudioGeneralInfoDTO> getAllAudiosFromPublicPlaylist(String playlistName);
    void makePlaylistPublic(String playlistName);

}