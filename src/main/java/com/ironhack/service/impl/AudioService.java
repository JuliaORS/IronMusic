package com.ironhack.service.impl;

import com.ironhack.exceptions.ResourceNotFoundException;
import com.ironhack.model.Audio;
import com.ironhack.repository.AudioRepository;
import com.ironhack.service.interfaces.AudioServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AudioService implements AudioServiceInterface {

    @Autowired
    AudioRepository audioRepository;

    @Override
    public void deleteAudio(Long id){
        Optional<Audio> AudioOptional = audioRepository.findById(id);
        if (AudioOptional.isPresent()){
            audioRepository.delete(AudioOptional.get());
        } else {
            throw new ResourceNotFoundException("Audio with ID " + id + " not found");
        }
    }
}
