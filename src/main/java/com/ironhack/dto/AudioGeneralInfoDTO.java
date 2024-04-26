package com.ironhack.dto;

import com.ironhack.model.Album;
import com.ironhack.model.Audio;
import lombok.Data;

@Data
public class AudioGeneralInfoDTO {

    private String title;
    private String duration;
    private String artistName;

    public AudioGeneralInfoDTO(Audio audio){
        setTitle(audio.getTitle());
        setDuration(audio.getDuration());
        setArtistName(audio.getArtist().getName());
    }
}
