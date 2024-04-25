package com.ironhack.dto;

import com.ironhack.demosecurityjwt.security.models.Artist;
import com.ironhack.model.Album;
import com.ironhack.model.Song;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class AlbumGeneralInfoDTO {
    private String title;

    private String artistName;

    private List<Song> songs;

    public AlbumGeneralInfoDTO(Album album){
        setTitle(album.getTitle());
        setArtistName(album.getArtist().getName());
        setSongs(album.getSongs());
    }
}
