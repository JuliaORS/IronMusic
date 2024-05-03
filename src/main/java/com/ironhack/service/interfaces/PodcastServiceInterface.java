package com.ironhack.service.interfaces;

import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.model.Podcast;

import java.util.List;

public interface PodcastServiceInterface {
    AudioGeneralInfoDTO savePodcast(Podcast podcast);

    List<AudioGeneralInfoDTO> getAllPodcasts();

    void deletePodcastByTitle(String title);
    List<AudioGeneralInfoDTO> getPodcastByTitle(String title);

    List<AudioGeneralInfoDTO> getPodcastByArtistName(String artist);
    public List<AudioGeneralInfoDTO> getPodcastByAllInfo(String info);
    
}
