package com.ironhack.dto;

import com.ironhack.model.Audio;
import com.ironhack.model.Song;
import lombok.Data;

@Data
public class SongGeneralInfoDTO {
    private String title;
    private String duration;
    private String artistName;
    private String albumTitle;
    private String genre;

    public SongGeneralInfoDTO(Song song){
        setTitle(song.getTitle());
        setDuration(song.getDuration());
        setArtistName(song.getArtist().getName());
        if (song.getAlbum() != null)
            setAlbumTitle(song.getAlbum().getTitle());
        else
            setAlbumTitle("no album associated");
        setGenre(song.getGenre());
    }
}
