package com.ironhack.security.repository;

import com.ironhack.security.utils.ArtistStatus;
import com.ironhack.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findByIsActiveFalse();
    List<User> findByArtistStatus(ArtistStatus status);
}
