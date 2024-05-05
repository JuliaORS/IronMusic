package com.ironhack.repository;

import com.ironhack.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song,Long> {
    List<Song> findByTitle(String title);
    List<Song> findByTitleContaining(String title); //check
    List<Song> findByTitleAndAlbumTitleAndAlbumArtistUsername(String songTitle, String albumTitle, String artistUsername);
    List<Song> findByTitleAndArtistUsername(String songTitle, String artistUsername);
    List<Song> findByArtistNameContaining(String artist); //check
    List<Song> findByArtistNameContainingOrTitleContainingOrAlbumTitleContaining(String artistName,
                                                                                 String title, String albumTitle);
    List<Song> findByGenreContaining(String genre);
}
