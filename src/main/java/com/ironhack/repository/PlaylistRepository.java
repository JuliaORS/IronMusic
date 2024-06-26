package com.ironhack.repository;

import com.ironhack.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist,Long> {
    List<Playlist> findByName(String name);
    List<Playlist> findByNameAndIsPrivateFalse(String title);
}
