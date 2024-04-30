package com.ironhack.demosecurityjwt.repositories;

import com.ironhack.demosecurityjwt.security.repositories.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.ironhack.demosecurityjwt.security.models.Role;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void saveAndDeleteRoleTest(){
        long initialResources = roleRepository.count();
        Role newRole = new Role("ROLE_NEW");
        roleRepository.save(newRole);
        assertEquals(initialResources + 1, roleRepository.count());
        Role roleToDelete = roleRepository.findByName("ROLE_NEW");
        roleRepository.delete(roleToDelete);
        assertEquals(initialResources, roleRepository.count());
    }

    @Test
    public void findByNameNotExistingNameTest(){
        Role newRole = roleRepository.findByName("hi");
        assertNull(newRole);
    }
}
