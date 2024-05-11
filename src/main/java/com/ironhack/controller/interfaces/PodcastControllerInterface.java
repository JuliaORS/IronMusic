package com.ironhack.controller.interfaces;

import com.ironhack.dto.PodcastGeneralInfoDTO;
import com.ironhack.model.Podcast;
import java.util.List;

public interface PodcastControllerInterface {
    PodcastGeneralInfoDTO savePodcast(Podcast podcast);
    List<PodcastGeneralInfoDTO> getAllPodcasts();
    void deletePodcastByTitle(String title);
    List<PodcastGeneralInfoDTO> getPodcastByTitle(String title);
    List<PodcastGeneralInfoDTO> getPodcastByArtistName(String artist);
    List<PodcastGeneralInfoDTO> getPodcastByAll(String info);
}
