package com.ironhack.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PlaylistTest {
    @Test
    public void playlistVoidConstructorTest(){
        Playlist playlist = new Playlist();
        assertNotNull(playlist);
    }

    @Test
    public void playlistConstructorTest(){
        Playlist playlist = Playlist.builder()
                .name("summer hits")
                .audios(null)
                .build();
        assertNotNull(playlist);
    }

    @Test
    public void playlistSetterGetterTest() {
        Audio audio = Audio.audioBuilder()
                .title("Title")
                .duration("3:23")
                .artist(null)
                .build();
        assertNotNull(audio);
        List<Audio> audios = new ArrayList<>();
        audios.add(audio);

        Playlist playlist = new Playlist();
        playlist.setName("summer hits");
        playlist.setAudios(audios);

        assertEquals("summer hits", playlist.getName());
        assertEquals("Title", playlist.getAudios().get(0).getTitle());
    }
}
