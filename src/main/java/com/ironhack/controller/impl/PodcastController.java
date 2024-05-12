package com.ironhack.controller.impl;

import com.ironhack.controller.interfaces.PodcastControllerInterface;
import com.ironhack.dto.PodcastGeneralInfoDTO;
import com.ironhack.model.Podcast;
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

    /*Actions only available to artists-Users*/
    @Override
    @PostMapping("/artist/podcast")
    @ResponseStatus(HttpStatus.CREATED)
    public PodcastGeneralInfoDTO savePodcast(@Valid @RequestBody Podcast podcast) {
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
    @GetMapping("/user/podcasts")
    @ResponseStatus(HttpStatus.OK)
    public List<PodcastGeneralInfoDTO> getAllPodcasts() {
        return podcastService.getAllPodcasts();
    }

    @Override
    @GetMapping("/user/podcast/title/{title}")
    @ResponseStatus(HttpStatus.OK)
    public List<PodcastGeneralInfoDTO> getPodcastByTitle(@PathVariable String title) {
        return podcastService.getPodcastByTitle(title);
    }

    @Override
    @GetMapping("/user/podcast/artist_name/{artistName}")
    @ResponseStatus(HttpStatus.OK)
    public List<PodcastGeneralInfoDTO> getPodcastByArtistName(@PathVariable String artistName) {
        return podcastService.getPodcastByArtistName(artistName);
    }

    @Override
    @GetMapping("/user/podcast/{info}")
    @ResponseStatus(HttpStatus.OK)
    public List<PodcastGeneralInfoDTO> getPodcastByAll(@PathVariable String info) {return podcastService.getPodcastByAllInfo(info);
    }
}
