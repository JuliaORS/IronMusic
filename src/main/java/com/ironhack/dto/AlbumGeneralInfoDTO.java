package com.ironhack.dto;

import com.ironhack.demosecurityjwt.security.models.Artist;
import com.ironhack.model.Album;
import com.ironhack.model.Song;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AlbumGeneralInfoDTO {
    private String title;

    private String artistName;

    private List<String> songTitle;

    public AlbumGeneralInfoDTO(Album album){
        setTitle(album.getTitle());
        setArtistName(album.getArtist().getName());
        setSongsTitleFromSongsList(album.getSongs());
    }

    public void setSongsTitleFromSongsList(List<Song> songList){
        songTitle = new ArrayList<>();
        for (Song song : songList){
            songTitle.add(song.getTitle());
        }
    }
}
