package com.ironhack.controller.interfaces;

import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.model.Podcast;

import java.util.List;

public interface PodcastControllerInterface {
    AudioGeneralInfoDTO savePodcast(Podcast podcast);
    List<AudioGeneralInfoDTO> getAllPodcasts();
    void deletePodcast(Long id);
    List<AudioGeneralInfoDTO> getPodcastByTitle(String title);
    List<AudioGeneralInfoDTO> getPodcastByArtistName(String artist);
}
