package com.ironhack.controller.interfaces;

import com.ironhack.dto.AudioGeneralInfoDTO;
import java.util.List;

public interface AudioControllerInterface {
    List<AudioGeneralInfoDTO> getAudioByAllInfo(String info);
}
