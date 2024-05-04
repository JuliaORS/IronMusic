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
    @DeleteMapping("/artist/audio/{title}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAudioByTitle(@PathVariable String title) {
        audioService.deleteAudioByTitle(title);
    }

    @Override
    @GetMapping("/user/audio/{info}")
    @ResponseStatus(HttpStatus.OK)
    public List<AudioGeneralInfoDTO> getAudioByAll(@PathVariable String info) {
        return audioService.getAudioByAllInfo(info);
    }
}
