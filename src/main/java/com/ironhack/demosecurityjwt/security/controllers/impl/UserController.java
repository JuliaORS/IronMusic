package com.ironhack.demosecurityjwt.security.controllers.impl;

import com.ironhack.demosecurityjwt.security.controllers.interfaces.UserControllerInterface;
import com.ironhack.demosecurityjwt.security.dtos.ArtistRoleAdmissionDTO;
import com.ironhack.demosecurityjwt.security.dtos.UserGeneralInfoDTO;
import com.ironhack.demosecurityjwt.security.models.Artist;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.services.interfaces.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RESTful API for User management
 */
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
    @ResponseStatus(value = HttpStatus.OK)
    public void activeUserByUsername(@PathVariable String username) {
        userService.activeUserByUsername(username);
    }

    @Override
    @PutMapping("/admin/user/active")
    @ResponseStatus(value = HttpStatus.OK)
    public void activeAllUsers() {
        userService.activeAllUsers();
    }

    @Override
    @PutMapping("/admin/artist/{username}")
    @ResponseStatus(value = HttpStatus.OK)
    public void activeArtistByUsername(@PathVariable String username) {
        userService.activeArtistByUsername(username);
    }

    @Override
    @PutMapping("/admin/artist/active")
    @ResponseStatus(value = HttpStatus.OK)
    public void activeAllArtists() {
        userService.activeAllArtists();
    }

    /*Actions available for standard-users*/
    @Override
    @GetMapping("/users/users")
    @ResponseStatus(HttpStatus.OK)
    public List<UserGeneralInfoDTO> getUsers() {
        return userService.getUsers();
    }

    @PutMapping("/users/artist")
    @ResponseStatus(HttpStatus.OK)
    public void requestToBeAnArtist(){
        userService.requestToBeAnArtist();
    }
}
