package com.ironhack.demosecurityjwt.security.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ironhack.model.Album;
import com.ironhack.model.Audio;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@PrimaryKeyJoinColumn(name="id")
public class Artist extends User {
    @OneToMany(mappedBy = "artist", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)  //cascade = CascadeType.ALL
    private List<Album> albums;


    @OneToMany(mappedBy = "artist", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)   //cascade = CascadeType.ALL
    //@JsonManagedReference // break circular reference
    private List<Audio> audios;

    public Artist(User user) {
        setId(user.getId());
        setName(user.getName());
        setUsername(user.getUsername());
        setPassword(user.getPassword());
        setRoles(user.getRoles());
        setPlaylists(user.getPlaylists());
    }
}
