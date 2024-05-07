package com.ironhack.security.dto;

import com.ironhack.security.utils.ArtistStatus;
import com.ironhack.security.utils.Status;
import com.ironhack.security.model.User;
import lombok.Data;

@Data
public class UserGeneralInfoDTO {
    private String name;

    private String username;

    private Status status;

    private ArtistStatus artistStatus;
    public UserGeneralInfoDTO(User user){
        setName(user.getName());
        setUsername(user.getUsername());
        if (user.isActive())
            setStatus(Status.ACTIVE);
        else
            setStatus(Status.INACTIVE);
        setArtistStatus(user.getArtistStatus());
    }
}
