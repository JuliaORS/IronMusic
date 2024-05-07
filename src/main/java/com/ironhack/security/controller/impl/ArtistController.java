package com.ironhack.security.controller.impl;

import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.security.controller.interfaces.ArtistControllerInterface;
import com.ironhack.security.service.impl.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ArtistController implements ArtistControllerInterface {
    @Autowired
    ArtistService artistService;

    @Override
    @GetMapping("/artist/profile")
    @ResponseStatus(HttpStatus.OK)
    public List<AudioGeneralInfoDTO> getOwnAudios() {
        return artistService.getOwnAudios();
    }

}
