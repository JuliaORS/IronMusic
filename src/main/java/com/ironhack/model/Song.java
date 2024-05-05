package com.ironhack.model;

import com.ironhack.security.model.Artist;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "songBuilder")
@PrimaryKeyJoinColumn(name="id")
public class Song extends Audio{

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    private String genre;

    public Song(String title, String duration, Artist artist, Album album, String genre) {
        super(title, duration, artist);
        setAlbum(album);
        setGenre(genre);
    }
}