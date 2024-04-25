package com.ironhack.model;

import com.ironhack.demosecurityjwt.security.models.Artist;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
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
    private int chapter;
    private String category;

    public Podcast(String title, String duration, Artist artist, int season, int chapter, String category){
        super(title, duration, artist);
        setSeason(season);
        setChapter(chapter);
        setCategory(category);
    }
}
