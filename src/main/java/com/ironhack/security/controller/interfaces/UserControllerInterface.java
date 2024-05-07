package com.ironhack.security.controller.interfaces;

import com.ironhack.security.dto.UserGeneralInfoDTO;
import com.ironhack.security.model.User;
import java.util.List;

public interface UserControllerInterface {
    void signUpUser(User user);
    void activeUserByUsername(String username);
    List<String> activeAllUsers();
    void activeArtistByUsername(String username);
    List<String> activeAllArtists();
    void requestToBeAnArtist();
    List<UserGeneralInfoDTO> getAllUsers();
    UserGeneralInfoDTO getUserByUsername(String username);
    UserGeneralInfoDTO getOwnProfile();
}
