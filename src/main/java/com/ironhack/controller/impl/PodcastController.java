package com.ironhack.controller.impl;

import com.ironhack.controller.interfaces.PodcastControllerInterface;
import com.ironhack.demosecurityjwt.security.services.interfaces.UserServiceInterface;
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
    public Podcast savePodcast(@Valid @RequestBody Podcast podcast) {
        return podcastService.savePodcast(podcast);
    }

    @Override
    @DeleteMapping("/artist/podcast/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePodcast(@PathVariable Long id) {
        podcastService.deletePodcast(id);
    }


    /*Actions available to standard-users*/
    @Override
    @GetMapping("/users/podcast")
    @ResponseStatus(HttpStatus.OK)
    public List<Podcast> getAllPodcasts() {
        return podcastService.getAllPodcasts();
    }

    @Override
    @GetMapping("/users/podcast/{title}")
    @ResponseStatus(HttpStatus.OK)
    public List<Podcast> getPodcastByTitle(@PathVariable String title) {
        return podcastService.getPodcastByTitle(title);
    }

    @Override
    @GetMapping("/users/podcast/{artist}")
    @ResponseStatus(HttpStatus.OK)
    public List<Podcast> getPodcastByArtist(@PathVariable String artist) {
        return podcastService.getPodcastByArtist(artist);
    }

}
