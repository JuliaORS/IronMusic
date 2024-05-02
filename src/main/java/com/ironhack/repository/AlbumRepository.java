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
    @Query("SELECT a FROM Album a WHERE a.title = :title AND a.artist.username = :username")
    List<Album> findByTitleAndArtistUsername(@Param("title") String title, @Param("username") String username);
}
