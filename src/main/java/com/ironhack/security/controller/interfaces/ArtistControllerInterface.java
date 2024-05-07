package com.ironhack.security.controller.interfaces;

import com.ironhack.dto.AudioGeneralInfoDTO;
import java.util.List;

public interface ArtistControllerInterface {
    List<AudioGeneralInfoDTO> getOwnAudios();
}
