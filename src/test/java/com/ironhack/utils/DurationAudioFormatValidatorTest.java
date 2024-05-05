package com.ironhack.utils;

import com.ironhack.exception.BadRequestFormatException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DurationAudioFormatValidatorTest {
    @Test
    public void validDurationTest() {
        String duration = "3:25";
        DurationAudioFormatValidator durationAudioFormatValidator = new DurationAudioFormatValidator(duration);
        assertNotNull(durationAudioFormatValidator);
    }

    @Test
    public void moreThanTwoNumbersInvalidTest() {
        String duration = "725:10";
        assertThrows(BadRequestFormatException.class, () -> {new DurationAudioFormatValidator(duration);});
    }

    @Test
    public void invalidCharactersTest() {
        String duration = "aa:10";
        assertThrows(BadRequestFormatException.class, () -> {new DurationAudioFormatValidator(duration);});
    }
}
