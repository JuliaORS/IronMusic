package com.ironhack.security.model;

import com.ironhack.security.utils.ArtistStatus;
import com.ironhack.model.Playlist;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static jakarta.persistence.FetchType.EAGER;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy=InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Bad request. Name is required.")
    @Size(min = 4, max = 25)
    private String name;

    @NotEmpty(message = "Bad request. Username is required.")
    private String username;

    @NotEmpty(message = "Bad request. Password is required.")
    private String password;

    private boolean isActive;

    @Enumerated
    private ArtistStatus artistStatus;

    @ManyToMany(fetch = EAGER)
    private Collection<Role> roles = new ArrayList<>();

    @ManyToMany(fetch = EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_playlist",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "playlist_id")
    )
    private List<Playlist> playlists = new ArrayList<>();
}