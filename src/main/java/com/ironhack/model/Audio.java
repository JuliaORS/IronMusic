package com.ironhack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ironhack.demosecurityjwt.security.models.Artist;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "audioBuilder")
@Inheritance(strategy=InheritanceType.JOINED)
public class Audio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotEmpty(message = "Bad request. Title is required.")
    private String title;

    @NotEmpty(message = "Bad request. Duration is required.")
    private String duration;

    @ManyToOne //(fetch = FetchType.EAGER)
    @JoinColumn(name = "artist_id")
    //@JsonIgnore // Ignore recursive property
    private Artist artist;

    public Audio(String title, String duration, Artist artist){
        setTitle(title);
        setDuration(duration);
        setArtist(artist);
    }
}
