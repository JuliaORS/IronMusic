package com.ironhack.model;

import com.ironhack.demosecurityjwt.security.models.Artist;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotEmpty(message = "Bad request. Title is required.")
    private String title;

    @ManyToOne //(fetch = FetchType.EAGER)
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @OneToMany(mappedBy = "album", cascade = CascadeType.REMOVE) //fetch = FetchType.EAGER, orphanRemoval = true
    private List<Song> songs;

    public Album(String title, Artist artist, List<Song> songs) {
        setTitle(title);
        setArtist(artist);
        setSongs(songs);
    }
}
