package com.ironhack.security.service.interfaces;

import com.ironhack.dto.AudioGeneralInfoDTO;
import com.ironhack.security.dto.UserGeneralInfoDTO;
import com.ironhack.security.model.Role;
import com.ironhack.security.model.User;

import java.util.List;

public interface UserServiceInterface {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    void activeUserByUsername(String username);
    List<String> activeAllUsers();
    void activeArtistByUsername(String username);
    List<String> activeAllArtists();
    void requestToBeAnArtist();
    List<UserGeneralInfoDTO> getAllUsers();
    UserGeneralInfoDTO getUserByUsername(String username);
    UserGeneralInfoDTO getOwnProfile();
}
