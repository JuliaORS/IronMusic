package com.ironhack.repository;

import com.ironhack.model.Audio;
import com.ironhack.model.Podcast;
import com.ironhack.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AudioRepository extends JpaRepository<Audio,Long> {
    List<Audio> findByTitleAndArtistUsername(String audioTitle, String artistUsername);
    List<Audio> findByTitleContaining(String title);
    List<Audio> findByArtistNameContainingOrTitleContaining(String artistName, String title);
    List<Audio> findByTitle(String title);
}
