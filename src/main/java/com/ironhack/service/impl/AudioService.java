package com.ironhack.service.impl;

import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.exceptions.ResourceNotFoundException;
import com.ironhack.model.Audio;
import com.ironhack.model.Podcast;
import com.ironhack.model.Song;
import com.ironhack.repository.AudioRepository;
import com.ironhack.service.interfaces.AudioServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AudioService implements AudioServiceInterface {
    @Autowired
    AudioRepository audioRepository;

    @Override
    public void deleteAudioByTitle(String title){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<Audio> audioList = audioRepository.findByTitleAndArtistUsername(title, username);
        if (audioList.size() == 1){
            audioRepository.delete(audioList.get(0));
        } else {
            throw new ResourceNotFoundException("Audio with title \"" + title + "\" not found");
        }
    }

    @Override
    public List<AudioGeneralInfoDTO> getAudioByAllInfo(String info) {
        List<Audio> audios = audioRepository.findByArtistNameContainingOrTitleContaining(info, info);
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
