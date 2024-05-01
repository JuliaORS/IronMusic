package com.ironhack.demosecurityjwt.security.services.impl;

import com.ironhack.demosecurityjwt.security.dtos.ArtistRoleAdmissionDTO;
import com.ironhack.demosecurityjwt.security.models.Artist;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.repositories.ArtistRepository;
import com.ironhack.demosecurityjwt.security.repositories.RoleRepository;
import com.ironhack.demosecurityjwt.security.repositories.UserRepository;
import com.ironhack.demosecurityjwt.security.services.interfaces.ArtistServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ArtistService implements ArtistServiceInterface {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Override
    public Artist assignArtistRole(ArtistRoleAdmissionDTO artistRoleAdmission){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).get();
        userService.addRoleToUser(username, "ROLE_ARTIST");

        Artist artist = new Artist(user);
        userRepository.delete(user);
        return artistRepository. save(artist);
        //return new Artist(user);
    }

}
