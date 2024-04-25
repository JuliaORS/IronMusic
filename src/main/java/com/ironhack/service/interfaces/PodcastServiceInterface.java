package com.ironhack.service.interfaces;

import com.ironhack.model.Podcast;

import java.util.List;

public interface PodcastServiceInterface {
    Podcast savePodcast(Podcast podcast);

    List<Podcast> getAllPodcasts();

    void deletePodcast(Long id);
    List<Podcast> getPodcastByTitle(String title);

    List<Podcast> getPodcastByArtist(String artist);
    
}
