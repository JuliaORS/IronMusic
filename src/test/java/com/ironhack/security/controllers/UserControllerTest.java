package com.ironhack.security.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.security.utils.ArtistStatus;
import com.ironhack.security.dto.UserGeneralInfoDTO;
import com.ironhack.security.model.Role;
import com.ironhack.security.model.User;
import com.ironhack.security.repository.ArtistRepository;
import com.ironhack.security.repository.RoleRepository;
import com.ironhack.security.repository.UserRepository;
import com.ironhack.security.service.impl.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        mockMvc.perform(put("/api/user/artist")
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
        mockMvc.perform(put("/api/user/artist")
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
        mockMvc.perform(put("/api/user/artist")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Artist is already pending to active")));
    }

    @Test
    public void getAllUsersTest() throws Exception{
        List<UserGeneralInfoDTO> userGeneralInfoDTOS = new ArrayList<>();
        List<User> userList = userRepository.findAll();
        for (User user : userList){
            userGeneralInfoDTOS.add(new UserGeneralInfoDTO(user));
        }
        String expectedJson = objectMapper.writeValueAsString(userGeneralInfoDTOS);

        mockMvc.perform(get("/api/user/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void getUserByUsernameExistingUserTest() throws Exception{
        String expectedJson = objectMapper.writeValueAsString(new UserGeneralInfoDTO(user));
        mockMvc.perform(get("/api/user/user/{username}", "username")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void getUserNotExistingUserTest() throws Exception{
        mockMvc.perform(get("/api/user/user/{username}", "wrong")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(Matchers.containsString("User with username \"wrong\" not found")));
    }

    @Test
    public void getOwnProfileTest() throws Exception{
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(user.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String expectedJson = objectMapper.writeValueAsString(new UserGeneralInfoDTO(user));
        mockMvc.perform(get("/api/user/profile")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }
}
