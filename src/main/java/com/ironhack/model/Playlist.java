package com.ironhack.model;

import com.ironhack.security.model.User;
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

    private boolean isPrivate = true;

    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable(
            name = "playlist_audio",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "audio_id")
    )
    private List<Audio> audios;

    @ManyToMany(mappedBy = "playlists", fetch = FetchType.EAGER)
    private List<User> users = new ArrayList<>();

    public Playlist(String name, List<Audio> audios, List<User> users){
        setName(name);
        setAudios(audios);
        setUsers(users);
        setPrivate(true);
    }
}
