package com.ironhack.security.service.impl;

import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.model.Audio;
import com.ironhack.security.repository.ArtistRepository;
import com.ironhack.security.repository.RoleRepository;
import com.ironhack.security.repository.UserRepository;
import com.ironhack.security.service.interfaces.ArtistServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ArtistService implements ArtistServiceInterface {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Override
    public List<AudioGeneralInfoDTO> getOwnAudios() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<Audio> audioList = artistRepository.findByUsername(username).get().getAudios();
        List<AudioGeneralInfoDTO> audioGeneralInfoDTOS = new ArrayList<>();
        for(Audio audio : audioList){
            audioGeneralInfoDTOS.add(new AudioGeneralInfoDTO(audio));
        }
        return audioGeneralInfoDTOS;
    }
}
