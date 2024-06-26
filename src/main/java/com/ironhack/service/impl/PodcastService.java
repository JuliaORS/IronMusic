package com.ironhack.service.impl;

import com.ironhack.dto.PodcastGeneralInfoDTO;
import com.ironhack.utils.DurationAudioFormatValidator;
import com.ironhack.security.model.Artist;
import com.ironhack.security.model.User;
import com.ironhack.security.repository.UserRepository;
import com.ironhack.exception.ResourceNotFoundException;
import com.ironhack.model.Podcast;
import com.ironhack.repository.PodcastRepository;
import com.ironhack.service.interfaces.PodcastServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class PodcastService implements PodcastServiceInterface {
    @Autowired
    private PodcastRepository podcastRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public PodcastGeneralInfoDTO savePodcast(@Valid Podcast podcast) {
        new DurationAudioFormatValidator(podcast.getDuration());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).get();
        podcast.setArtist((Artist) user);
        podcastRepository.save(podcast);
        return new PodcastGeneralInfoDTO(podcast);
    }

    @Override
    public void deletePodcastByTitle(String title){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Podcast podcast = podcastRepository.findByTitleAndArtistUsername(title, username)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Podcast with title \"" + title + "\" not found"));
        podcastRepository.delete(podcast);
    }

    @Override
    public List<PodcastGeneralInfoDTO> getAllPodcasts() {
        List<PodcastGeneralInfoDTO> result = new ArrayList<>();
        List<Podcast> podcasts = podcastRepository.findAll();
        for(Podcast podcast : podcasts){
            result.add(new PodcastGeneralInfoDTO(podcast));
        }
        return result;
    }

    @Override
    public List<PodcastGeneralInfoDTO> getPodcastByTitle(String title) {
        List<Podcast> podcasts = podcastRepository.findByTitleContaining(title);
        List<PodcastGeneralInfoDTO> result = new ArrayList<>();
        if (podcasts.isEmpty()){
            throw new ResourceNotFoundException("No podcasts found with that title.");
        } else {
            for(Podcast podcast : podcasts){
                result.add(new PodcastGeneralInfoDTO(podcast));
            }
        }
        return result;
    }

    @Override
    public List<PodcastGeneralInfoDTO> getPodcastByArtistName(String artist) {
        List<Podcast> podcasts = podcastRepository.findByArtistNameContaining(artist);
        List<PodcastGeneralInfoDTO> result = new ArrayList<>();
        if (podcasts.isEmpty()){
            throw new ResourceNotFoundException("No podcasts found with that artist name.");
        } else {
            for(Podcast podcast : podcasts){
                result.add(new PodcastGeneralInfoDTO(podcast));
            }
        }
        return result;
    }

    @Override
    public List<PodcastGeneralInfoDTO> getPodcastByAllInfo(String info) {
        List<Podcast> podcasts = podcastRepository.findByArtistNameContainingOrTitleContaining(info, info);
        List<PodcastGeneralInfoDTO> result = new ArrayList<>();
        if (podcasts.isEmpty()){
            throw new ResourceNotFoundException("No podcasts found with that info.");
        } else {
            for(Podcast podcast : podcasts){
                result.add(new PodcastGeneralInfoDTO(podcast));
            }
        }
        return result;
    }
}
