package com.ironhack.service.interfaces;

import com.ironhack.dto.AudioGeneralInfoDTO;

import java.util.List;

public interface AudioServiceInterface {
    void deleteAudioByTitle(String title);
    List<AudioGeneralInfoDTO> getAudioByAllInfo(String info);


}
