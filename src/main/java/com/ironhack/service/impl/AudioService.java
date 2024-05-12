package com.ironhack.service.impl;

import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.exception.ResourceNotFoundException;
import com.ironhack.model.Audio;
import com.ironhack.repository.AudioRepository;
import com.ironhack.service.interfaces.AudioServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class AudioService implements AudioServiceInterface {
    @Autowired
    AudioRepository audioRepository;

    @Override
    public List<AudioGeneralInfoDTO> getAudioByAllInfo(String info) {
        List<Audio> audios = audioRepository.findByArtistUsernameContainingOrTitleContaining(info, info);
        List<AudioGeneralInfoDTO> result = new ArrayList<>();
        if (audios.isEmpty()){
            throw new ResourceNotFoundException("No audios found with that info.");
        } else {
            for(Audio audio : audios){
                result.add(new AudioGeneralInfoDTO(audio));
            }
        }
        return result;
    }
}
