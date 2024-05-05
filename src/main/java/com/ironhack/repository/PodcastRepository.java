package com.ironhack.repository;

import com.ironhack.model.Podcast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PodcastRepository extends JpaRepository<Podcast,Long> {
    List<Podcast> findByTitle(String title);
    List<Podcast> findByTitleContaining(String title);
    List<Podcast> findByArtistNameContaining(String artist);
    List<Podcast> findByArtistNameContainingOrTitleContaining(String artistName, String title);
    List<Podcast> findByTitleAndArtistUsername(String podcastTitle, String artistUsername);
}
