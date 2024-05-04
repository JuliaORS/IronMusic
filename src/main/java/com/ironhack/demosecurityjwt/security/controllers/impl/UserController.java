package com.ironhack.demosecurityjwt.security.controllers.impl;

import com.ironhack.demosecurityjwt.security.controllers.interfaces.UserControllerInterface;
import com.ironhack.demosecurityjwt.security.dtos.UserGeneralInfoDTO;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.services.interfaces.UserServiceInterface;
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
    @ResponseStatus(value = HttpStatus.OK)
    public void activeUserByUsername(@PathVariable String username) {
        userService.activeUserByUsername(username);
    }

    @Override
    @PutMapping("/admin/user/active")
    @ResponseStatus(value = HttpStatus.OK)
    public List<String> activeAllUsers() {
        return userService.activeAllUsers();
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

}
