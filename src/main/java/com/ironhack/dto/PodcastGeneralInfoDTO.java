package com.ironhack.dto;

import com.ironhack.model.Podcast;
import lombok.Data;

@Data
public class PodcastGeneralInfoDTO {
    private String title;
    private String duration;
    private String artistName;
    private int season;
    private int episode;
    private String category;

    public PodcastGeneralInfoDTO(Podcast podcast){
        setTitle(podcast.getTitle());
        setDuration(podcast.getDuration());
        setArtistName(podcast.getArtist().getName());
        setSeason(podcast.getSeason());
        setEpisode(podcast.getEpisode());
        setCategory(podcast.getCategory());
    }
}
