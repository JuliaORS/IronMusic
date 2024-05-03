package com.ironhack.controller.impl;

import com.ironhack.controller.interfaces.AlbumControllerInterface;
import com.ironhack.controller.interfaces.AudioControllerInterface;
import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.service.impl.AudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AudioController implements AudioControllerInterface {

    @Autowired
    AudioService audioService;

    @Override
    @DeleteMapping("/users/artist/audio/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAudioByTitle(@PathVariable String title) {
        audioService.deleteAudioByTitle(title);
    }

    @Override
    @GetMapping("/users/audio/{info}")
    @ResponseStatus(HttpStatus.OK)
    public List<AudioGeneralInfoDTO> getAudioByAll(@PathVariable String info) {
        return audioService.getAudioByAllInfo(info);
    }
}
