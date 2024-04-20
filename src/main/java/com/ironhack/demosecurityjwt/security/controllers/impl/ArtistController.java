package com.ironhack.demosecurityjwt.security.controllers.impl;

import com.ironhack.demosecurityjwt.security.controllers.interfaces.ArtistControllerInterface;
import com.ironhack.demosecurityjwt.security.controllers.interfaces.RoleControllerInterface;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.services.impl.ArtistService;
import com.ironhack.model.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/artist")
public class ArtistController implements ArtistControllerInterface {

    @Autowired
    ArtistService artistService;

}
