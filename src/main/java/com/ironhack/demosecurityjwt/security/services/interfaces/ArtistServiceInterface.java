package com.ironhack.demosecurityjwt.security.services.interfaces;

import com.ironhack.demosecurityjwt.security.dtos.ArtistRoleAdmissionDTO;
import com.ironhack.demosecurityjwt.security.models.Artist;


public interface ArtistServiceInterface {
    Artist assignArtistRole(ArtistRoleAdmissionDTO artistRoleAdmission);

}
