package com.ironhack.service.interfaces;

import com.ironhack.dto.PodcastGeneralInfoDTO;
import com.ironhack.model.Podcast;
import java.util.List;

public interface PodcastServiceInterface {
    PodcastGeneralInfoDTO savePodcast(Podcast podcast);
    List<PodcastGeneralInfoDTO> getAllPodcasts();
    void deletePodcastByTitle(String title);
    List<PodcastGeneralInfoDTO> getPodcastByTitle(String title);
    List<PodcastGeneralInfoDTO> getPodcastByArtistName(String artist);
    public List<PodcastGeneralInfoDTO> getPodcastByAllInfo(String info);
}
