package com.ironhack.dto;

import com.ironhack.model.Album;
import com.ironhack.model.Audio;
import com.ironhack.model.Playlist;
import com.ironhack.model.Song;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class PlaylistGeneralInfoDTO {

    private String name;

    private Map<String, String> audioArtistList;
    public PlaylistGeneralInfoDTO(Playlist playlist){
        setName(playlist.getName());
        setAudioArtistMapFromAudioList(playlist.getAudios());
    }

    public void setAudioArtistMapFromAudioList(List<Audio> audioList){
        audioArtistList = new HashMap<>();
        for (Audio audio : audioList){
            audioArtistList.put(audio.getTitle(), audio.getArtist().getName());
        }
    }
}
