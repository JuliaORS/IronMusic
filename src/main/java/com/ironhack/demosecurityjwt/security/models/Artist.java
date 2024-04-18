package com.ironhack.demosecurityjwt.security.models;

import com.ironhack.model.Album;
import com.ironhack.model.Audio;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Artist extends User {
    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    private List<Album> albums;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    private List<Audio> audios;
}
