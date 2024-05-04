package com.ironhack.repository;

import com.ironhack.model.Album;
import com.ironhack.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album,Long>{
    List<Album> findByTitle(String title);
    List<Album> findByTitleAndArtistUsername(String title, String username);
    List<Album> findByTitleContaining(String title);
    List<Album> findByArtistNameContaining(String title);
    List<Album> findByArtistNameContainingOrTitleContaining(String artistName, String title);
}
