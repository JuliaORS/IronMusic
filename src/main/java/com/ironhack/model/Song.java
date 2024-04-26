package com.ironhack.model;

import com.ironhack.demosecurityjwt.security.models.Artist;
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

    @ManyToOne //(fetch = FetchType.EAGER)
    @JoinColumn(name = "album_id")
    private Album album;
    private String genre;

    public Song(String title, String duration, Artist artist, Album album, String genre) {
        super(title, duration, artist);
        setAlbum(album);
        setGenre(genre);
    }
}