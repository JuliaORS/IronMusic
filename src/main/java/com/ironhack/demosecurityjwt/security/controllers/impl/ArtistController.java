package com.ironhack.demosecurityjwt.security.controllers.impl;

import com.ironhack.demosecurityjwt.security.controllers.interfaces.ArtistControllerInterface;
import com.ironhack.demosecurityjwt.security.services.impl.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ArtistController implements ArtistControllerInterface {
    @Autowired
    ArtistService artistService;



}
