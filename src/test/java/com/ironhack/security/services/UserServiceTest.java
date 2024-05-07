package com.ironhack.security.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.security.utils.ArtistStatus;
import com.ironhack.security.dto.UserGeneralInfoDTO;
import com.ironhack.security.exception.ArtistActivationException;
import com.ironhack.security.exception.UserNotFoundException;
import com.ironhack.security.model.Role;
import com.ironhack.security.model.User;
import com.ironhack.security.repository.ArtistRepository;
import com.ironhack.security.repository.RoleRepository;
import com.ironhack.security.repository.UserRepository;
import com.ironhack.security.service.impl.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private ArtistRepository artistRepository;

    @Autowired
    private RoleRepository roleRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private User user;
    @BeforeEach
    public void setUp(){
        user = new User(null, "user", "username", "1234",
                true, ArtistStatus.INACTIVE,  null, null);
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
        assertThrows(UserNotFoundException.class, () -> userService.loadUserByUsername("wrong"));
    }

    @Test
    public void saveUserTest(){
        long currentUsers = userRepository.count();
        User newUser = new User(null, "userNew", "usernameNew", "1234",
                false, ArtistStatus.INACTIVE, null, null);
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
        assertThrows(UserNotFoundException.class, () ->
                userService.addRoleToUser("wrong", "ROLE_ARTIST"));
    }

    @Test
    public void activeUserTest(){
        assertFalse(userRepository.findByUsername("username").get().isActive());
        userService.activeUserByUsername("username");
        assertTrue(userRepository.findByUsername("username").get().isActive());
    }

    @Test
    public void activeUserNotExistingTest(){
        assertThrows(UserNotFoundException.class, () -> {
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

    @Test
    public void activeArtistByUsernameTest(){
        User user = userRepository.findByUsername("username").get();
        user.setArtistStatus(ArtistStatus.PENDING_ACTIVATION);
        userRepository.save(user);
        assertEquals(ArtistStatus.PENDING_ACTIVATION, userRepository.findByUsername("username").get().getArtistStatus());
        userService.activeArtistByUsername("username");
        assertEquals(ArtistStatus.ACTIVE, userRepository.findByUsername("username").get().getArtistStatus());
    }

    @Test
    public void activeArtistNotExistingTest(){
        assertThrows(UserNotFoundException.class, () -> {
            userService.activeArtistByUsername("wrong username");});
    }

    @Test
    public void activeArtistAlreadyActiveTest(){
        User user = userRepository.findByUsername("username").get();
        user.setArtistStatus(ArtistStatus.ACTIVE);
        userRepository.save(user);
        assertThrows(ArtistActivationException.class, () -> {
            userService.activeArtistByUsername("username");});
    }

    @Test
    public void activeArtistNotPendingToActiveTest(){
        User user = userRepository.findByUsername("username").get();
        user.setArtistStatus(ArtistStatus.INACTIVE);
        userRepository.save(user);
        assertThrows(ArtistActivationException.class, () -> {
            userService.activeArtistByUsername("username");});
    }

    @Test
    public void activeAllArtistTest() throws Exception {
        User user = userRepository.findByUsername("username").get();
        user.setArtistStatus(ArtistStatus.PENDING_ACTIVATION);
        userRepository.save(user);
        List<String> usernames = new ArrayList<>();
        usernames.add(user.getUsername());
        String expectedJson = objectMapper.writeValueAsString(usernames);
        String resultJson = objectMapper.writeValueAsString(userService.activeAllArtists());
        assertEquals(expectedJson, resultJson);
        assertEquals(ArtistStatus.ACTIVE, userRepository.findByUsername("username").get().getArtistStatus());
        assertEquals(ArtistStatus.ACTIVE, artistRepository.findByUsername("username").get().getArtistStatus());
        assertTrue(artistRepository.findByUsername("username").get().isActive());
        assertTrue(userRepository.findByUsername("username").get().isActive());
        assertEquals(2, userRepository.findByUsername("username").get().getRoles().size());
        assertEquals(2, artistRepository.findByUsername("username").get().getRoles().size());
    }

    @Test
    public void requestToBeAnArtistTest(){
        assertEquals(ArtistStatus.INACTIVE, userRepository.findByUsername("username").get().getArtistStatus());
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(user.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        userService.requestToBeAnArtist();
        assertEquals(ArtistStatus.PENDING_ACTIVATION, userRepository.findByUsername("username").get().getArtistStatus());
    }

    @Test
    public void getAllUsersTest() throws Exception{
        List<User> userList = userRepository.findAll();
        List<UserGeneralInfoDTO> userGeneralInfoDTOS = new ArrayList<>();
        for(User user : userList){
            userGeneralInfoDTOS.add(new UserGeneralInfoDTO(user));
        }
        String expectedJson = objectMapper.writeValueAsString(userGeneralInfoDTOS);
        String resultJson = objectMapper.writeValueAsString(userService.getAllUsers());
        assertEquals(expectedJson, resultJson);
    }

    @Test
    public void getUserByUsernameExistingUserTest() throws Exception{
        Optional<User> optionalUser = userRepository.findByUsername("username");
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            String expectedJson = objectMapper.writeValueAsString(new UserGeneralInfoDTO(user));
            assertEquals(user.getName(), userService.getUserByUsername("username").getName());
            String resultJson = objectMapper.writeValueAsString(userService.getUserByUsername("username"));
            assertEquals(expectedJson, resultJson);
        }
    }

    @Test
    public void getUserByUsernameNotExistingUserTest(){
        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserByUsername("wrong username");});
    }
}
