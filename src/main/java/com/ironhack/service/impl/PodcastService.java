package com.ironhack.service.impl;

import com.ironhack.Utils.Validator;
import com.ironhack.demosecurityjwt.security.models.Artist;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.repositories.UserRepository;
import com.ironhack.exceptions.BadRequestFormatException;
import com.ironhack.exceptions.ResourceNotFoundException;
import com.ironhack.model.Podcast;
import com.ironhack.repository.PodcastRepository;
import com.ironhack.service.interfaces.PodcastServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PodcastService implements PodcastServiceInterface {
    @Autowired
    private PodcastRepository podcastRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public Podcast savePodcast(@Valid Podcast podcast) {
        if (!Validator.durationAudioValidator(podcast.getDuration())) {
            throw new BadRequestFormatException("Bad request. Duration has not a correct format: HH:MM:SS or MM:SS or SS");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        podcast.setArtist((Artist) user);
        return podcastRepository.save(podcast);
    }

    @Override
    public List<Podcast> getAllPodcasts() {
        return podcastRepository.findAll();
    }

    @Override
    public void deletePodcast(Long id){
        Optional<Podcast> podcastOptional = podcastRepository.findById(id);
        if (podcastOptional.isPresent()){
            podcastRepository.delete(podcastOptional.get());
        } else {
            throw new ResourceNotFoundException("Podcast with ID " + id + " not found");
        }
    }
    @Override
    public List<Podcast> getPodcastByTitle(String title) {
        List<Podcast> podcasts = podcastRepository.findByTitleContaining(title);
        if (podcasts.isEmpty()){
            throw new ResourceNotFoundException("No Podcasts found with that title.");
        } else {
            return podcasts;
        }
    }

    @Override
    public List<Podcast> getPodcastByArtist(String artist) {
        List<Podcast> podcasts = podcastRepository.findByArtistContaining(artist);
        if (podcasts.isEmpty()){
            throw new ResourceNotFoundException("No Podcasts found of that artist.");
        } else {
            return podcasts;
        }
    }
}
