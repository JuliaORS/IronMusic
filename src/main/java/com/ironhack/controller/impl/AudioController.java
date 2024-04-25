package com.ironhack.controller.impl;

import com.ironhack.controller.interfaces.AlbumControllerInterface;
import com.ironhack.controller.interfaces.AudioControllerInterface;
import com.ironhack.service.impl.AudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AudioController implements AudioControllerInterface {

    @Autowired
    AudioService audioService;

    @Override
    @DeleteMapping("/users/artist/audio/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAudio(@PathVariable Long id) {
        audioService.deleteAudio(id);
    }
}
