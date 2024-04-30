package com.ironhack.demosecurityjwt.security.dtos;

import com.ironhack.demosecurityjwt.security.Utils.Status;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.model.Song;
import lombok.Data;
import java.util.List;

@Data
public class UserGeneralInfoDTO {
    private String name;

    private String username;

    private Status status;
    public UserGeneralInfoDTO(User user){
        setName(user.getName());
        setUsername(user.getUsername());
        if (user.isActive())
            setStatus(Status.ACTIVE);
        else
            setStatus(Status.INACTIVE);
    }
}
