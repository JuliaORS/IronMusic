package com.ironhack.demosecurityjwt.security.models;

import com.ironhack.demosecurityjwt.security.Utils.ArtistStatus;
import com.ironhack.model.Playlist;
import jakarta.persistence.*;
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

    private String name;

    private String username;

    private String password;

    private boolean isActive;

    @Enumerated
    private ArtistStatus artistStatus;

    @ManyToMany(fetch = EAGER)
    private Collection<Role> roles = new ArrayList<>();

    @ManyToMany(fetch = EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "playlist_user", //join table name
            joinColumns = @JoinColumn(name = "playlist_id"), // ref column this entity
            inverseJoinColumns = @JoinColumn(name = "user_id") // audio ref column
    )
    private List<Playlist> playlists;
}