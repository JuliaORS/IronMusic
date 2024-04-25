package com.ironhack.repository;

import com.ironhack.exceptions.ResourceNotFoundException;
import com.ironhack.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song,Long> {
    List<Song> findByTitleContaining(String title);
    List<Song> findByArtistContaining(String artist);
    List<Song> findByGenreContaining(String genre);
}
