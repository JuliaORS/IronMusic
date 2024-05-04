package com.ironhack.demosecurityjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.demosecurityjwt.security.Utils.ArtistStatus;
import com.ironhack.demosecurityjwt.security.dtos.UserGeneralInfoDTO;
import com.ironhack.demosecurityjwt.security.exceptions.ArtistActivationException;
import com.ironhack.demosecurityjwt.security.exceptions.UserNotFoundException;
import com.ironhack.demosecurityjwt.security.models.Artist;
import com.ironhack.demosecurityjwt.security.models.Role;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.repositories.ArtistRepository;
import com.ironhack.demosecurityjwt.security.repositories.RoleRepository;
import com.ironhack.demosecurityjwt.security.repositories.UserRepository;
import com.ironhack.demosecurityjwt.security.services.impl.UserService;
import com.ironhack.dto.AlbumGeneralInfoDTO;
import com.ironhack.model.Album;
import com.ironhack.model.Song;
import com.ironhack.repository.AlbumRepository;
import com.ironhack.repository.SongRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UserControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserService userService;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private User user;

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

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
        artistRepository.deleteAll();
    }

    @Test
    public void signUpTest() throws Exception{
        User newUser = new User(null, "userNew", "usernameNew", "1234",
                false, ArtistStatus.INACTIVE, null, null);
        String userJson = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post("/api/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void activeUserByUsernameTest() throws Exception{
        mockMvc.perform(put("/api/admin/user/{username}", "username")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertTrue(userRepository.findByUsername("username").get().isActive());
    }

   @Test
    public void activeUserByUsernameNotExistingTest() throws Exception{
        mockMvc.perform(put("/api/admin/user/{username}", "wrong")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString(
                        "User with username \"wrong\" not found")));
    }

    @Test
    public void activeAllUsersTest() throws Exception{
        mockMvc.perform(put("/api/admin/user/active")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertTrue(userRepository.findByUsername("username").get().isActive());
    }

    @Test
    public void activeArtistByUsernameTest() throws Exception{
        User user = userRepository.findByUsername("username").get();
        user.setArtistStatus(ArtistStatus.PENDING_ACTIVATION);
        userRepository.save(user);
        mockMvc.perform(put("/api/admin/artist/{username}", "username")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(ArtistStatus.ACTIVE, userRepository.findByUsername("username").get().getArtistStatus());
    }

    @Test
    public void activeArtistNotExistingTest() throws Exception{
        mockMvc.perform(put("/api/admin/artist/{username}", "wrong")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("User with username \"wrong\" not found")));
    }

    @Test
    public void activeArtistAlreadyActiveTest() throws Exception{
        User user = userRepository.findByUsername("username").get();
        user.setArtistStatus(ArtistStatus.ACTIVE);
        userRepository.save(user);
        mockMvc.perform(put("/api/admin/artist/{username}", "username")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Artist is already ACTIVE")));
    }

    @Test
    public void activeArtistNotPendingToActiveTest() throws Exception{
        User user = userRepository.findByUsername("username").get();
        user.setArtistStatus(ArtistStatus.INACTIVE);
        userRepository.save(user);
        mockMvc.perform(put("/api/admin/artist/{username}", "username")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Artist is not pending to active")));
    }

    @Test
    public void activeAllArtistTest() throws Exception{
        User user = userRepository.findByUsername("username").get();
        user.setArtistStatus(ArtistStatus.PENDING_ACTIVATION);
        userRepository.save(user);
        mockMvc.perform(put("/api/admin/artist/active")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(ArtistStatus.ACTIVE, userRepository.findByUsername("username").get().getArtistStatus());
    }

    @Test
    public void requestToBeAnArtistTest() throws Exception{
        assertEquals(ArtistStatus.INACTIVE, userRepository.findByUsername("username").get().getArtistStatus());
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(user.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        mockMvc.perform(put("/api/users/artist")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(ArtistStatus.PENDING_ACTIVATION, userRepository.findByUsername("username").get().getArtistStatus());
    }

    @Test
    public void requestToBeAnArtistAlreadyActiveTest() throws Exception{
        User user = userRepository.findByUsername("username").get();
        user.setArtistStatus(ArtistStatus.ACTIVE);
        userRepository.save(user);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(user.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        mockMvc.perform(put("/api/users/artist")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Artist is already ACTIVE")));
    }

    @Test
    public void requestToBeAnArtistAlreadyPendingTest() throws Exception{
        User user = userRepository.findByUsername("username").get();
        user.setArtistStatus(ArtistStatus.PENDING_ACTIVATION);
        userRepository.save(user);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(user.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        mockMvc.perform(put("/api/users/artist")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Artist is already pending to active")));
    }

    @Test
    public void getUserExistingUserTest() throws Exception{

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(user.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        mockMvc.perform(put("/api/user/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Artist is already pending to active")));


        Optional<User> optionalUser = userRepository.findByUsername("username");
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            String expectedJson = objectMapper.writeValueAsString(new UserGeneralInfoDTO(user));
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


/*



    @Override
    @GetMapping("/users/users")
    @ResponseStatus(HttpStatus.OK)
    public List<UserGeneralInfoDTO> getUsers() {
        return userService.getUsers();
    }

    @Override
    @GetMapping("/users/user/{username}")
    @ResponseStatus(HttpStatus.OK)
    public UserGeneralInfoDTO getUser(@PathVariable String username) {
        return userService.getUser(username);
    }*/

}
