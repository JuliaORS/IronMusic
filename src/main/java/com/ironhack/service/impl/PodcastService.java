package com.ironhack.service.impl;

import com.ironhack.Utils.Validator;
import com.ironhack.demosecurityjwt.security.models.Artist;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.repositories.UserRepository;
import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.exceptions.BadRequestFormatException;
import com.ironhack.exceptions.ResourceNotFoundException;
import com.ironhack.model.Audio;
import com.ironhack.model.Podcast;
import com.ironhack.model.Song;
import com.ironhack.repository.PodcastRepository;
import com.ironhack.service.interfaces.PodcastServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PodcastService implements PodcastServiceInterface {
    @Autowired
    private PodcastRepository podcastRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public AudioGeneralInfoDTO savePodcast(@Valid Podcast podcast) {
        if (!Validator.durationAudioValidator(podcast.getDuration())) {
            throw new BadRequestFormatException("Bad request. Duration has not a correct format: HH:MM:SS or MM:SS or SS");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).get();
        podcast.setArtist((Artist) user);
        podcastRepository.save(podcast);
        return new AudioGeneralInfoDTO(podcast);
    }

    @Override
    public void deletePodcastByTitle(String title){
        Optional<Podcast> podcastOptional = podcastRepository.findByTitle(title);
        if (podcastOptional.isPresent()){
            podcastRepository.delete(podcastOptional.get());
        } else {
            throw new ResourceNotFoundException("Podcast with title \"" + title + "\" not found");
        }
    }

    @Override
    public List<AudioGeneralInfoDTO> getAllPodcasts() {
        List<AudioGeneralInfoDTO> result = new ArrayList<>();
        List<Podcast> podcasts = podcastRepository.findAll();
        for(Podcast podcast : podcasts){
            result.add(new AudioGeneralInfoDTO(podcast));
        }
        return result;
    }


    @Override
    public List<AudioGeneralInfoDTO> getPodcastByTitle(String title) {
        List<Podcast> podcasts = podcastRepository.findByTitleContaining(title);
        List<AudioGeneralInfoDTO> result = new ArrayList<>();
        if (podcasts.isEmpty()){
            throw new ResourceNotFoundException("No podcasts found with that title.");
        } else {
            for(Podcast podcast : podcasts){
                result.add(new AudioGeneralInfoDTO(podcast));
            }
        }
        return result;
    }

    @Override
    public List<AudioGeneralInfoDTO> getPodcastByArtistName(String artist) {
        List<Podcast> podcasts = podcastRepository.findByArtistNameContaining(artist);
        List<AudioGeneralInfoDTO> result = new ArrayList<>();
        if (podcasts.isEmpty()){
            throw new ResourceNotFoundException("No podcasts found with that artist name.");
        } else {
            for(Podcast podcast : podcasts){
                result.add(new AudioGeneralInfoDTO(podcast));
            }
        }
        return result;
    }

    @Override
    public List<AudioGeneralInfoDTO> getPodcastByAllInfo(String info) {
        List<Podcast> podcasts = podcastRepository.findByArtistNameContainingOrTitleContaining(info, info);
        List<AudioGeneralInfoDTO> result = new ArrayList<>();
        if (podcasts.isEmpty()){
            throw new ResourceNotFoundException("No podcasts found with that info.");
        } else {
            for(Podcast podcast : podcasts){
                result.add(new AudioGeneralInfoDTO(podcast));
            }
        }
        return result;
    }
}
