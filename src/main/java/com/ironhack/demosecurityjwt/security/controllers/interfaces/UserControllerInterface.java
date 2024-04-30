package com.ironhack.demosecurityjwt.security.controllers.interfaces;

import com.ironhack.demosecurityjwt.security.dtos.UserGeneralInfoDTO;
import com.ironhack.demosecurityjwt.security.models.User;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface UserControllerInterface {
    List<UserGeneralInfoDTO> getUsers();
    void signUpUser(User user);
    void activeUser(Long id);
}
