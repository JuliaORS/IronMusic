package com.ironhack.model;

import com.ironhack.demosecurityjwt.security.models.Artist;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Audio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private double duration;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "artist_id")
    Artist artist;
}
