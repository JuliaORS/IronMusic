package com.ironhack.utils;

import com.ironhack.exception.BadRequestFormatException;

public class DurationAudioFormatValidator {
    public DurationAudioFormatValidator(String duration) {
        if (!duration.isEmpty()){
            String[] splitDuration = duration.split(":");
            if (splitDuration.length > 3)
                throw new BadRequestFormatException("Bad request. Duration has not a correct format: HH:MM:SS or MM:SS or SS");
            for (String nb : splitDuration){
                if (!nb.matches("\\d+") || nb.length() > 2){
                    throw new BadRequestFormatException("Bad request. Duration has not a correct format: HH:MM:SS or MM:SS or SS");
                }
            }
        }
    }
}
