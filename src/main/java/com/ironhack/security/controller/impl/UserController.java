package com.ironhack.security.controller.impl;

import com.ironhack.security.controller.interfaces.UserControllerInterface;
import com.ironhack.security.dto.UserGeneralInfoDTO;
import com.ironhack.security.model.User;
import com.ironhack.security.service.interfaces.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController implements UserControllerInterface {
    @Autowired
    private UserServiceInterface userService;

    /*Actions available for all(before being registered)*/
    @Override
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUpUser(@RequestBody User user) {
        userService.saveUser(user);
    }

    /*Actions only available for admin-User*/
    @Override
    @PutMapping("/admin/user/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void activeUserByUsername(@PathVariable String username) {
        userService.activeUserByUsername(username);
    }

    @Override
    @PutMapping("/admin/user/active")
    @ResponseStatus(HttpStatus.OK)
    public List<String> activeAllUsers() {
        return userService.activeAllUsers();
    }

    @Override
    @PutMapping("/admin/artist/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void activeArtistByUsername(@PathVariable String username) {
        userService.activeArtistByUsername(username);
    }

    @Override
    @PutMapping("/admin/artist/active")
    @ResponseStatus(HttpStatus.OK)
    public List<String> activeAllArtists() {
        return userService.activeAllArtists();
    }

    /*Actions available for standard-users*/

    @PutMapping("/user/artist")
    @ResponseStatus(HttpStatus.OK)
    public void requestToBeAnArtist(){
        userService.requestToBeAnArtist();
    }

    @Override
    @GetMapping("/user/users")
    @ResponseStatus(HttpStatus.OK)
    public List<UserGeneralInfoDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @Override
    @GetMapping("/user/user/{username}")
    @ResponseStatus(HttpStatus.OK)
    public UserGeneralInfoDTO getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @Override
    @GetMapping("/user/profile")
    @ResponseStatus(HttpStatus.OK)
    public UserGeneralInfoDTO getOwnProfile() {
        return userService.getOwnProfile();
    }
}
