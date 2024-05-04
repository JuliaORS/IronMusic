package com.ironhack.demosecurityjwt.security.services.interfaces;

import com.ironhack.demosecurityjwt.security.Utils.ArtistStatus;
import com.ironhack.demosecurityjwt.security.dtos.UserGeneralInfoDTO;
import com.ironhack.demosecurityjwt.security.models.Role;
import com.ironhack.demosecurityjwt.security.models.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserServiceInterface {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    User getUser(String username);
    List<UserGeneralInfoDTO> getUsers();
    void activeUserByUsername(String username);
    List<String> activeAllUsers();
    void activeArtistByUsername(String username);
    List<String> activeAllArtists();
    void requestToBeAnArtist();
}
