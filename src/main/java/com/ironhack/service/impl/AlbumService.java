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
    public void deleteAlbum(Long id){
        Optional<Album> albumOptional = albumRepository.findById(id);
        if (albumOptional.isPresent()){
            albumRepository.delete(albumOptional.get());
        } else {
            throw new ResourceNotFoundException("Album with ID " + id + " not found");
        }
    }

    @Override
    public  void addSongToAlbum(Long albumId, Long songId){
        Optional<Album> albumOptional = albumRepository.findById(albumId);
        if (albumOptional.isPresent()){
            Optional<Song> songOptional = songRepository.findById(songId);
            if (songOptional.isPresent()) {
                Album album = albumOptional.get();
                album.getSongs().add(songOptional.get());
            } else {
                throw new ResourceNotFoundException("Song with ID " + songId + " not found");
            }
        } else {
            throw new ResourceNotFoundException("Album with ID " + albumId + " not found");
        }
    }

    @Override
    public  void removeSongFromAlbum(Long albumId, Long songId){
        Optional<Album> albumOptional = albumRepository.findById(albumId);
        if (albumOptional.isPresent()){
            Album album = albumOptional.get();
            boolean removed = album.getSongs().removeIf(audio -> audio.getId().equals(songId));
            if (!removed) {
                throw new ResourceNotFoundException("Song with ID " + songId + " not found in album");
            }
        } else {
            throw new ResourceNotFoundException("Album with ID " + albumId + " not found");
        }
    }
}
