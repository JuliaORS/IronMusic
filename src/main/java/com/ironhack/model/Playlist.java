package com.ironhack.model;

import com.ironhack.demosecurityjwt.security.models.Artist;
import com.ironhack.demosecurityjwt.security.models.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.util.ArrayList;

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

    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable(
            name = "playlist_audio", // Nombre de la tabla de unión
            joinColumns = @JoinColumn(name = "playlist_id"), // Columna que hace referencia al ID del usuario
            inverseJoinColumns = @JoinColumn(name = "audio_id") // Columna que hace referencia al ID de la lista de reproducción
    )
    private List<Audio> audios;

    @ManyToMany(mappedBy = "playlists", fetch = FetchType.EAGER)
    private List<User> users = new ArrayList<>();

    public Playlist(String name, List<Audio> audios, List<User> users){
        setName(name);
        setAudios(audios);
        setUsers(users);
    }
    public Playlist(String name, List<Audio> audios){
        setName(name);
        setAudios(audios);
    }
}
