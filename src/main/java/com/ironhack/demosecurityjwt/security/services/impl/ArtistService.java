package com.ironhack.demosecurityjwt.security.services.impl;

import com.ironhack.demosecurityjwt.security.repositories.ArtistRepository;
import com.ironhack.demosecurityjwt.security.repositories.RoleRepository;
import com.ironhack.demosecurityjwt.security.repositories.UserRepository;
import com.ironhack.demosecurityjwt.security.services.interfaces.ArtistServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
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

}
