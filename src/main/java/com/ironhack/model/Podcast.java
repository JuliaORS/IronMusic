package com.ironhack.model;

import com.ironhack.security.model.Artist;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "podcastBuilder")
@PrimaryKeyJoinColumn(name="id")
public class Podcast extends Audio{

    private int season;
    private int episode;
    @NotEmpty(message = "Bad request. Category is required.")
    private String category;

    public Podcast(String title, String duration, Artist artist, int season, int episode, String category){
        super(title, duration, artist);
        setSeason(season);
        setEpisode(episode);
        setCategory(category);
    }
}
