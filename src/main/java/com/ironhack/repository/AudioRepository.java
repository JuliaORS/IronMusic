package com.ironhack.repository;

import com.ironhack.model.Audio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface AudioRepository extends JpaRepository<Audio,Long> {
    List<Audio> findByTitleAndArtistUsername(String audioTitle, String artistUsername);
    List<Audio> findByTitleContaining(String title);
    List<Audio> findByArtistNameContainingOrTitleContaining(String artistName, String title);
    List<Audio> findByTitle(String title);
    @Query("SELECT a FROM Audio a JOIN a.playlist p JOIN p.users u WHERE a.title = " +
            ":audioTitle AND p.name = :playlistTitle AND u.username = :username")
    List<Audio> findByTitleAndPlaylistTitleAndUserUsername(@Param("audioTitle") String audioTitle,
                                                           @Param("playlistTitle") String playlistTitle,
                                                           @Param("username") String username);
}
