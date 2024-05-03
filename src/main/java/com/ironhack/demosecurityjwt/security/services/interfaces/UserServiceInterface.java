package com.ironhack.demosecurityjwt.security.services.interfaces;

import com.ironhack.demosecurityjwt.security.dtos.UserGeneralInfoDTO;
import com.ironhack.demosecurityjwt.security.models.Role;
import com.ironhack.demosecurityjwt.security.models.User;
import java.util.List;

public interface UserServiceInterface {


    User saveUser(User user);

    Role saveRole(Role role);

    void addRoleToUser(String username, String roleName);

    User getUser(String username);

    List<UserGeneralInfoDTO> getUsers();
    void activeUserByUsername(String username);
    List<String> activeAllUsers();
}
