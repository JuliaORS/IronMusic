package com.ironhack.service.impl;

import com.ironhack.demosecurityjwt.security.models.Artist;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.repositories.UserRepository;
import com.ironhack.dto.AlbumGeneralInfoDTO;
import com.ironhack.exceptions.ResourceNotFoundException;
import com.ironhack.model.Album;
import com.ironhack.model.Playlist;
import com.ironhack.model.Song;
import com.ironhack.repository.AlbumRepository;
import com.ironhack.repository.SongRepository;
import com.ironhack.service.interfaces.AlbumServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Optional;

@Service
public class AlbumService implements AlbumServiceInterface {

    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private SongRepository songRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public AlbumGeneralInfoDTO saveAlbum(@Valid Album album) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).get();
        album.setArtist((Artist) user);
        albumRepository.save(album);
        return new AlbumGeneralInfoDTO(album);
    }

    @Override
    public void deleteAlbumByTitle(String title){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<Album> albumList = albumRepository.findByTitleAndArtistUsername(title, username);
        if (albumList.size() == 1){
            albumRepository.delete(albumList.get(0));
        } else {
            throw new ResourceNotFoundException("Album with this title " + title + " not found");
        }
    }

    @Override
    public  void addSongToAlbum(String titleAlbum, String titleSong){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<Album> albumList = albumRepository.findByTitleAndArtistUsername(titleAlbum, username);
        if (albumList.size() == 1) {
            List<Song> songList = songRepository.findByTitleAndAlbumTitleAndAlbumArtistUsername(titleSong, titleAlbum, username);
            if (songList.size() == 1){
                albumList.get(0).getSongs().add(songList.get(0));
            } else {
                throw new ResourceNotFoundException("Song with title " + titleSong + " not found");
            }
        } else {
            throw new ResourceNotFoundException("Album with ID " + titleAlbum + " not found");
        }
    }

    @Override
    public  void removeSongFromAlbum(String titleAlbum, String titleSong){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<Album> albumList = albumRepository.findByTitleAndArtistUsername(titleAlbum, username);
        if (albumList.size() == 1) {
            List<Song> songList = songRepository.findByTitleAndAlbumTitleAndAlbumArtistUsername(titleSong, titleAlbum, username);
            if (songList.size() == 1){
                albumList.get(0).getSongs().remove(songList.get(0));
            } else {
                throw new ResourceNotFoundException("Song with title " + titleSong + " not found");
            }
        } else {
            throw new ResourceNotFoundException("Album with title " + titleAlbum + " not found");
        }
    }
}
