package com.ironhack.security.controller.impl;

import com.ironhack.security.controller.interfaces.ArtistControllerInterface;
import com.ironhack.security.service.impl.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ArtistController implements ArtistControllerInterface {
    @Autowired
    ArtistService artistService;



}
