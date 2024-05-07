package com.ironhack.repository;

import com.ironhack.model.Audio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AudioRepository extends JpaRepository<Audio,Long> {
    List<Audio> findByTitle(String title);
    List<Audio> findByTitleContaining(String title);
    List<Audio> findByArtistUsername(String artistUsername);
    List<Audio> findByArtistUsernameContainingOrTitleContaining(String artistUsername, String title);
}
