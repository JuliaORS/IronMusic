package com.ironhack.Utils;

public class Validator {

    public static boolean durationAudioValidator(String duration){
        if (duration.isEmpty()){
            return false;
        } else {
            String[] splitDuration = duration.split(":");
            if (splitDuration.length > 3)
                return false;
            for (String nb : splitDuration){
                if (!nb.matches("\\d+") || nb.length() > 2){
                    return false;
                }
            }
        }
        return true;
    }
}
