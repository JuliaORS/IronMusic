package com.ironhack.service.impl;

import com.ironhack.exceptions.ResourceNotFoundException;
import com.ironhack.model.Album;
import com.ironhack.model.Song;
import com.ironhack.repository.AlbumRepository;
import com.ironhack.repository.SongRepository;
import com.ironhack.service.interfaces.AlbumServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @Override
    public Album saveAlbum(Album album) {
        return albumRepository.save(album);
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
    public void deleteAlbum(Long id){
        Optional<Album> albumOptional = albumRepository.findById(id);
        if (albumOptional.isPresent()){
            albumRepository.delete(albumOptional.get());
        } else {
            throw new ResourceNotFoundException("Album with ID " + id + " not found");
        }
    }

}
