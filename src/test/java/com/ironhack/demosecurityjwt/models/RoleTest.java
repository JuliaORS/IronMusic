package com.ironhack.demosecurityjwt.models;

import com.ironhack.demosecurityjwt.security.models.Artist;
import com.ironhack.demosecurityjwt.security.models.Role;
import com.ironhack.model.Album;
import com.ironhack.model.Audio;
import com.ironhack.model.Song;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RoleTest {
    @Test
    public void roleConstructorVoidTest(){
        Role role = new Role();
        assertNotNull(role);
    }

    @Test
    public void roleConstructorTest(){
        Role role = new Role("ROLE_USER");
        assertNotNull(role);
        assertEquals("ROLE_USER", role.getName());
    }

    @Test
    public void roleSetterGetterTest(){
        List<Audio> songs = new ArrayList<>();
        Role role = new Role();
        role.setName("ROLE_USER");

        assertEquals("ROLE_USER", role.getName());
    }
}
