package com.ironhack.demosecurityjwt.security.controllers.impl;

import com.ironhack.demosecurityjwt.security.controllers.interfaces.UserControllerInterface;
import com.ironhack.demosecurityjwt.security.dtos.UserGeneralInfoDTO;
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

    @Override
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUpUser(@RequestBody User user) {
        userService.saveUser(user);
    }

    @Override
    @PutMapping("/admin/user/{username}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void activeUserByUsername(@PathVariable String username) {
        userService.activeUserByUsername(username);
    }

    @Override
    @PutMapping("/admin/user/active")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void activeAllUsers() {
        userService.activeAllUsers();
    }

    @Override
    @GetMapping("/users/users")
    @ResponseStatus(HttpStatus.OK)
    public List<UserGeneralInfoDTO> getUsers() {
        return userService.getUsers();
    }
}
