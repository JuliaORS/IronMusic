package com.ironhack.security.service.interfaces;


import com.ironhack.dto.AudioGeneralInfoDTO;
import java.util.List;

public interface ArtistServiceInterface {
    List<AudioGeneralInfoDTO> getOwnAudios();
}
