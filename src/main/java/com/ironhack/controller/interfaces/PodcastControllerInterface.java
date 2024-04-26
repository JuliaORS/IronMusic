package com.ironhack.controller.interfaces;

import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.model.Podcast;

import java.util.List;

public interface PodcastControllerInterface {
    AudioGeneralInfoDTO savePodcast(Podcast podcast);
    List<Podcast> getAllPodcasts();
    void deletePodcast(Long id);
    List<Podcast> getPodcastByTitle(String title);
    List<Podcast> getPodcastByArtist(String artist);
}
