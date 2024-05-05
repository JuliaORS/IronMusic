package com.ironhack.service.impl;

import com.ironhack.security.model.Artist;
import com.ironhack.security.model.User;
import com.ironhack.security.repository.UserRepository;
import com.ironhack.dto.AlbumGeneralInfoDTO;
import com.ironhack.exception.ResourceNotFoundException;
import com.ironhack.model.Album;
import com.ironhack.model.Song;
import com.ironhack.repository.AlbumRepository;
import com.ironhack.repository.SongRepository;
import com.ironhack.service.interfaces.AlbumServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

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
        Album album = albumRepository.findByTitleAndArtistUsername(title, username)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Album with title \"" + title + "\" not found"));
        albumRepository.delete(album);
    }

    @Override
    public  void addSongToAlbumByTitleSong(String albumTitle, String songTitle){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Album album = albumRepository.findByTitleAndArtistUsername(albumTitle, username)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Album with title \"" + albumTitle + "\" not found"));
        Song song = songRepository.findByTitleAndArtistUsername(songTitle, username)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Song with title \"" + songTitle + "\" not found"));
        List<Album> albumList = albumRepository.findByTitleAndArtistUsername(albumTitle, username);
        song.setAlbum(album);
        songRepository.save(song);
        album.getSongs().add(song);
        albumRepository.save(albumList.get(0));
    }

    @Override
    public  void removeSongFromAlbum(String albumTitle, String songTitle){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Album album = albumRepository.findByTitleAndArtistUsername(albumTitle, username)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Album with title \"" + albumTitle + "\" not found"));
        Song song = songRepository.findByTitleAndAlbumTitleAndAlbumArtistUsername(songTitle, albumTitle, username)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Song with title \"" + songTitle + "\" not found"));
        album.getSongs().remove(song);
        albumRepository.save(album);
        song.setAlbum(null);
        songRepository.save(song);
    }

    @Override
    public List<AlbumGeneralInfoDTO> getAllAlbums() {
        List<AlbumGeneralInfoDTO> result = new ArrayList<>();
        List<Album> albums = albumRepository.findAll();
        for(Album album : albums){
            result.add(new AlbumGeneralInfoDTO(album));
        }
        return result;
    }


    @Override
    public List<AlbumGeneralInfoDTO> getAlbumByTitle(String title) {
        List<Album> albums = albumRepository.findByTitleContaining(title);
        List<AlbumGeneralInfoDTO> result = new ArrayList<>();
        if (albums.isEmpty()){
            throw new ResourceNotFoundException("No albums found with that title.");
        } else {
            for(Album album : albums){
                result.add(new AlbumGeneralInfoDTO(album));
            }
        }
        return result;
    }

    @Override
    public List<AlbumGeneralInfoDTO> getAlbumByArtistName(String artist) {
        List<Album> albums = albumRepository.findByArtistNameContaining(artist);
        List<AlbumGeneralInfoDTO> result = new ArrayList<>();
        if (albums.isEmpty()){
            throw new ResourceNotFoundException("No albums found with that artist name.");
        } else {
            for(Album album : albums){
                result.add(new AlbumGeneralInfoDTO(album));
            }
        }
        return result;
    }

    @Override
    public List<AlbumGeneralInfoDTO> getAlbumByAllInfo(String info) {
        List<Album> albums = albumRepository.findByArtistNameContainingOrTitleContaining(info, info);
        List<AlbumGeneralInfoDTO> result = new ArrayList<>();
        if (albums.isEmpty()){
            throw new ResourceNotFoundException("No albums found with that info.");
        } else {
            for(Album album : albums){
                result.add(new AlbumGeneralInfoDTO(album));
            }
        }
        return result;
    }
}
