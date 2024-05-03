package com.ironhack.demosecurityjwt.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.demosecurityjwt.security.dtos.UserGeneralInfoDTO;
import com.ironhack.demosecurityjwt.security.models.Role;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.repositories.RoleRepository;
import com.ironhack.demosecurityjwt.security.repositories.UserRepository;
import com.ironhack.demosecurityjwt.security.services.impl.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.security.core.userdetails.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private User user;
    @BeforeEach
    public void setUp(){
        user = new User(null, "user", "username", "1234",
                true, true,  null, null);
        Collection<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("ROLE_USER").get());
        user.setRoles(roles);
        userService.saveUser(user);
    }

    @AfterEach
    public void tearsDown(){
        userRepository.deleteAll();
    }

    @Test
    void loadUserByUsernameExistingUserTest() {
        UserDetails userDetails = userService.loadUserByUsername("username");
        assertNotNull(userDetails);
        assertEquals("username", userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void loadUserByUsernameNotExistingUserTest() {
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("wrong"));
    }

    @Test
    public void saveUserTest(){
        long currentUsers = userRepository.count();
        User newUser = new User(null, "userNew", "usernameNew", "1234",
                false, false, null, null);
        userService.saveUser(newUser);
        assertEquals(currentUsers + 1, userRepository.count());
    }

    @Test
    public void saveRoleTest(){
        Long currentRoleNumber = roleRepository.count();
        Role newRole = new Role("NEW_ROLE");
        assertEquals(newRole, userService.saveRole(newRole));
        assertEquals(currentRoleNumber + 1, roleRepository.count());
    }

    @Test
    public void addRoleToUserExistingUserTest(){
        userService.addRoleToUser("username", "ROLE_ARTIST");
        assertEquals(2, userRepository.findByUsername("username").get().getRoles().size());
    }

    @Test
    public void addRoleToUserNotExistingUserTest(){
        assertThrows(UsernameNotFoundException.class, () ->
                userService.addRoleToUser("wrong", "ROLE_ARTIST"));
    }

    @Test
    public void getUserExistingUserTest() throws Exception{
        Optional<User> optionalUser = userRepository.findByUsername("username");
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            String expectedJson = objectMapper.writeValueAsString(user);
            assertEquals(user.getName(), userService.getUser("username").getName());
            String resultJson = objectMapper.writeValueAsString(userService.getUser("username"));
            assertEquals(expectedJson, resultJson);
        }
    }

    @Test
    public void getUserNotExistingUserTest(){
        assertNull(userService.getUser("wrong"));
    }

    @Test
    public void getUsersTest() throws Exception{
        List<User> userList = userRepository.findAll();
        List<UserGeneralInfoDTO> userGeneralInfoDTOS = new ArrayList<>();
        for(User user : userList){
            userGeneralInfoDTOS.add(new UserGeneralInfoDTO(user));
        }
        String expectedJson = objectMapper.writeValueAsString(userGeneralInfoDTOS);
        String resultJson = objectMapper.writeValueAsString(userService.getUsers());
        assertEquals(expectedJson, resultJson);
    }

    @Test
    public void activeUserTest(){
        assertFalse(userRepository.findByUsername("username").get().isActive());
        userService.activeUserByUsername("username");
        assertTrue(userRepository.findByUsername("username").get().isActive());
    }

    @Test
    public void activeUserNotExistingTest(){
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.activeUserByUsername("wrong username");});
    }

    @Test
    public void activeAllUsersTest() throws Exception {
        User user = userRepository.findByUsername("username").get();
        List<String> usernames = new ArrayList<>();
        usernames.add(user.getUsername());
        String expectedJson = objectMapper.writeValueAsString(usernames);
        String resultJson = objectMapper.writeValueAsString(userService.activeAllUsers());
        assertEquals(expectedJson, resultJson);
        assertTrue(userRepository.findByUsername("username").get().isActive());
    }
}
