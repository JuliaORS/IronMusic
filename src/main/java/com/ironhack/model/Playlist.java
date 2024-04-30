package com.ironhack.model;

import com.ironhack.demosecurityjwt.security.models.Artist;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotEmpty(message = "Bad request. Playlist name is required.")
    private String name;

    @ManyToMany //(cascade = CascadeType.ALL)
    @JoinTable(
            name = "playlist_audio", //join table name
            joinColumns = @JoinColumn(name = "playlist_id"), // ref column this entity
            inverseJoinColumns = @JoinColumn(name = "audio_id") // audio ref column
    )
    private List<Audio> audios;

    public Playlist(String name, List<Audio> audios){
        setName(name);
        setAudios(audios);
    }
}
