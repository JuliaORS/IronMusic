package com.ironhack.controller.impl;

import com.ironhack.controller.interfaces.PodcastControllerInterface;
import com.ironhack.demosecurityjwt.security.services.interfaces.UserServiceInterface;
import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.model.Podcast;
import com.ironhack.repository.PodcastRepository;
import com.ironhack.service.impl.PodcastService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PodcastController implements PodcastControllerInterface {

   @Autowired
    private PodcastService podcastService;
    @Autowired
    private PodcastRepository podcastRepository;
    @Autowired
    private UserServiceInterface userService;

    /*Actions only available to artists-Users*/
    @Override
    @PostMapping("/artist/podcast")
    @ResponseStatus(HttpStatus.CREATED)
    public AudioGeneralInfoDTO savePodcast(@Valid @RequestBody Podcast podcast) {
        return podcastService.savePodcast(podcast);
    }

    @Override
    @DeleteMapping("/artist/podcast/{title}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePodcastByTitle(@PathVariable String title) {
        podcastService.deletePodcastByTitle(title);
    }


    /*Actions available to standard-users*/
    @Override
    @GetMapping("/users/podcasts")
    @ResponseStatus(HttpStatus.OK)
    public List<AudioGeneralInfoDTO> getAllPodcasts() {
        return podcastService.getAllPodcasts();
    }

    @Override
    @GetMapping("/users/podcast/title/{title}")
    @ResponseStatus(HttpStatus.OK)
    public List<AudioGeneralInfoDTO> getPodcastByTitle(@PathVariable String title) {
        return podcastService.getPodcastByTitle(title);
    }

    @Override
    @GetMapping("/users/podcast/artist_name/{artistName}")
    @ResponseStatus(HttpStatus.OK)
    public List<AudioGeneralInfoDTO> getPodcastByArtistName(@PathVariable String artistName) {
        return podcastService.getPodcastByArtistName(artistName);
    }

    @Override
    @GetMapping("/users/podcast/{info}")
    @ResponseStatus(HttpStatus.OK)
    public List<AudioGeneralInfoDTO> getPodcastByAll(@PathVariable String info) {
     return podcastService.getPodcastByAllInfo(info);
    }
}
