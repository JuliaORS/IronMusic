package com.ironhack.demosecurityjwt.security.services.impl;

import com.ironhack.demosecurityjwt.security.dtos.UserGeneralInfoDTO;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.models.Role;
import com.ironhack.demosecurityjwt.security.repositories.RoleRepository;
import com.ironhack.demosecurityjwt.security.repositories.UserRepository;
import com.ironhack.demosecurityjwt.security.services.interfaces.UserServiceInterface;
import com.ironhack.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserServiceInterface, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Injects a bean of type PasswordEncoder into this class.
     * The bean is used for encoding passwords before storing them.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Loads the user by its username
     *
     * @param username the username to search for
     * @return the UserDetails object that matches the given username
     * @throws UsernameNotFoundException if the user with the given username is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Retrieve user with the given username
        Optional<User> optionalUser = userRepository.findByUsername(username);
        // Check if user exists
        if (optionalUser.isEmpty()) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User with username \"" + username + "\" not found");
        } else {
            User user = optionalUser.get();
            log.info("User found in the database: {}", username);
            // Create a collection of SimpleGrantedAuthority objects from the user's roles
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            user.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            });
            // Return the user details, including the username, password, and authorities
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
        }
    }

    @Override
    public User saveUser(User user) {
        log.info("Saving new user {} to the database", user.getName());
        // Encode the user's password for security before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(false);
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role {} to the database", role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to user {}", roleName, username);
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            Optional<Role> optionalRole = roleRepository.findByName(roleName);
            if (optionalRole.isPresent()){
                Role role = optionalRole.get();
                user.getRoles().add(role);
                userRepository.save(user);
            }
        } else {
            throw new UsernameNotFoundException("User with username \"" + username + "\" not found");
        }
    }

    @Override
    public User getUser(String username) {
        log.info("Fetching user {}", username);
        Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser.orElse(null);
    }

    @Override
    public List<UserGeneralInfoDTO> getUsers() {
        log.info("Fetching all users");
        List<User> users = userRepository.findAll();
        List<UserGeneralInfoDTO> userGeneralInfoDTOS = new ArrayList<>();
        for(User user : users){
            userGeneralInfoDTOS.add(new UserGeneralInfoDTO(user));
        }
        return userGeneralInfoDTOS;
    }

    @Override
    public void activeUserByUsername(String username){
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setActive(true);
            userRepository.save(user);
            addRoleToUser(user.getUsername(), "ROLE_USER");
        } else {
            throw new UsernameNotFoundException("User with username \"" + username + "\" not found");
        }
    }

    @Override
    public List<String> activeAllUsers(){
        List<User> inactiveUsers = userRepository.findByIsActiveFalse();
        if (!inactiveUsers.isEmpty()){
            List<String> usernamesModified = new ArrayList<>();
            for (User user : inactiveUsers){
                user.setActive(true);
                userRepository.save(user);
                addRoleToUser(user.getUsername(), "ROLE_USER");
                usernamesModified.add(user.getUsername());
            }
            return usernamesModified;
        }
        return null;
    }
}
