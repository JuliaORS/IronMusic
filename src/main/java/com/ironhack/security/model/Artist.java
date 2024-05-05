package com.ironhack.security.model;

import com.ironhack.security.utils.ArtistStatus;
import com.ironhack.model.Album;
import com.ironhack.model.Audio;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@PrimaryKeyJoinColumn(name="id")
public class Artist extends User {
    @OneToMany(mappedBy = "artist", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Album> albums;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Audio> audios;

    public Artist(User user) {
        setName(user.getName());
        setUsername(user.getUsername());
        setPassword(user.getPassword());
        setActive(true);
        setArtistStatus(ArtistStatus.ACTIVE);
    }
}
