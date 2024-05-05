package com.ironhack.repository;

import com.ironhack.model.Audio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface AudioRepository extends JpaRepository<Audio,Long> {
    List<Audio> findByTitle(String title);
    List<Audio> findByTitleAndArtistUsername(String audioTitle, String artistUsername);
    List<Audio> findByTitleContaining(String title);
    List<Audio> findByArtistNameContainingOrTitleContaining(String artistName, String title);

}
