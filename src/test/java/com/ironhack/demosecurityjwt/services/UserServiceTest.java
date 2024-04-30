package com.ironhack.demosecurityjwt.services;

import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.repositories.UserRepository;
import com.ironhack.demosecurityjwt.security.services.impl.UserService;
import com.ironhack.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    private User user;
    @BeforeEach
    public void setUp(){
        long currentUsers = userRepository.count();
        user = new User(null, "user", "username", "1234", false, null, null);
        userService.saveUser(user);
        assertEquals(currentUsers + 1, userRepository.count());
    }

    @AfterEach
    public void tearsDown(){
        userRepository.deleteAll();
    }

    @Test
    public void saveUserTest(){
        long currentUsers = userRepository.count();
        User newUser = new User(null, "userNew", "usernameNew", "1234", false, null, null);
        userService.saveUser(newUser);
        assertEquals(currentUsers + 1, userRepository.count());
    }

    @Test
    public void activeUserTest(){
        Long id = userRepository.findByUsername("username").getId();
        assertFalse(userRepository.findByUsername("username").isActive());
        userService.activeUser(id);
        assertTrue(userRepository.findByUsername("username").isActive());
    }

    @Test
    public void activeUserNotExistingTest(){
        assertThrows(ResourceNotFoundException .class, () -> {
            userService.activeUser(45L);});
    }
}
