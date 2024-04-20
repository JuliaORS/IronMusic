package com.ironhack.demosecurityjwt.security.repositories;

import com.ironhack.demosecurityjwt.security.models.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
}
