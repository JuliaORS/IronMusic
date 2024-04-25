package com.ironhack.repository;

import com.ironhack.model.Podcast;
import com.ironhack.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PodcastRepository extends JpaRepository<Podcast,Long> {
    List<Podcast> findByTitleContaining(String title);

    List<Podcast> findByArtistContaining(String artist);

}
