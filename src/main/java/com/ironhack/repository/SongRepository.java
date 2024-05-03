package com.ironhack.repository;

import com.ironhack.exceptions.ResourceNotFoundException;
import com.ironhack.model.Album;
import com.ironhack.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song,Long> {
    List<Song> findByTitleContaining(String title);
    Optional<Song> findByTitle(String title);
    List<Song> findByTitleAndAlbumTitleAndAlbumArtistUsername(String songTitle, String albumTitle, String artistUsername);
    List<Song> findByTitleAndArtistUsername(String songTitle, String artistUsername);
    List<Song> findByArtistNameContaining(String artist);
    List<Song> findByArtistNameContainingOrTitleContainingOrAlbumTitleContaining(String artistName,
                                                                                 String title, String albumTitle);
    List<Song> findByGenreContaining(String genre);
    List<Song> findByAlbumTitle(String albumTitle);
    List<Song> findByAlbumTitleAndTitle(String albumTitle, String songTitle);
}
