package com.ironhack.security.service.impl;

import com.ironhack.security.dto.UserGeneralInfoDTO;
import com.ironhack.security.exception.ArtistActivationException;
import com.ironhack.security.utils.ArtistStatus;
import com.ironhack.security.model.Artist;
import com.ironhack.security.model.User;
import com.ironhack.security.model.Role;
import com.ironhack.security.repository.ArtistRepository;
import com.ironhack.security.repository.RoleRepository;
import com.ironhack.security.repository.UserRepository;
import com.ironhack.security.service.interfaces.UserServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.ironhack.security.exception.UserNotFoundException;
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
    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User with username \"" + username + "\" not found");
        } else {
            User user = optionalUser.get();
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            user.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            });
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
        }
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(false);
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username \"" + username + "\" not found"));
        Optional<Role> optionalRole = roleRepository.findByName(roleName);
        if (optionalRole.isPresent()){
            user.getRoles().add(optionalRole.get());
            userRepository.save(user);
        }
    }
    @Override
    public void activeUserByUsername(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username \"" + username + "\" not found"));
        user.setActive(true);
        userRepository.save(user);
        addRoleToUser(user.getUsername(), "ROLE_USER");
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

    @Override
    public void activeArtistByUsername(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username \"" + username + "\" not found"));
        if (user.getArtistStatus() == ArtistStatus.PENDING_ACTIVATION){
            addRoleToUser(user.getUsername(), "ROLE_ARTIST");
            Artist artist = new Artist(user);
            userRepository.delete(user);
            artistRepository.save(artist);
        } else if (user.getArtistStatus() == ArtistStatus.ACTIVE) {
            throw new ArtistActivationException("Artist is already ACTIVE");
        } else {
            throw new ArtistActivationException("Artist is not pending to active");
        }
    }

    @Override
    public List<String> activeAllArtists(){
        List<User> pendingArtistList = userRepository.findByArtistStatus(ArtistStatus.PENDING_ACTIVATION);
        if (!pendingArtistList.isEmpty()){
            List<String> usernamesModified = new ArrayList<>();
            for (User user : pendingArtistList){
                addRoleToUser(user.getUsername(), "ROLE_ARTIST");
                Artist artist = new Artist(user);
                artistRepository.save(artist);
                userRepository.delete(user);
                usernamesModified.add(artist.getUsername());
            }
            return usernamesModified;
        }
        return null;
    }

    @Override
    public void requestToBeAnArtist(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).get();
        if (user.getArtistStatus() == ArtistStatus.INACTIVE){
            user.setArtistStatus(ArtistStatus.PENDING_ACTIVATION);
            userRepository.save(user);
        } else if (user.getArtistStatus() == ArtistStatus.ACTIVE) {
            throw new ArtistActivationException("Artist is already ACTIVE");
        } else {
            throw new ArtistActivationException("Artist is already pending to active");
        }
    }

    @Override
    public List<UserGeneralInfoDTO> getAllUsers() {
        log.info("Fetching all users");
        List<User> users = userRepository.findAll();
        List<UserGeneralInfoDTO> userGeneralInfoDTOS = new ArrayList<>();
        for(User user : users){
            userGeneralInfoDTOS.add(new UserGeneralInfoDTO(user));
        }
        return userGeneralInfoDTOS;
    }

    @Override
    public UserGeneralInfoDTO getUserByUsername(String username) {
        log.info("Fetching user {}", username);
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()){
            return new UserGeneralInfoDTO(optionalUser.get());
        } else {
            throw new UserNotFoundException("User with username \"" + username + "\" not found");
        }
    }
}