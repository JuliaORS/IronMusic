package com.ironhack.controller.interfaces;

import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.model.Podcast;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface PodcastControllerInterface {
    AudioGeneralInfoDTO savePodcast(Podcast podcast);
    List<AudioGeneralInfoDTO> getAllPodcasts();
    void deletePodcastByTitle(String title);
    List<AudioGeneralInfoDTO> getPodcastByTitle(String title);
    List<AudioGeneralInfoDTO> getPodcastByArtistName(String artist);
    List<AudioGeneralInfoDTO> getPodcastByAll(String info);
}
