package com.ironhack.security.repositories;

import com.ironhack.security.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.ironhack.security.model.Role;
import java.util.Optional;
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
        Optional<Role> optionalRoleToDelete = roleRepository.findByName("ROLE_NEW");
        if (optionalRoleToDelete.isPresent()){
            roleRepository.delete(optionalRoleToDelete.get());
            assertEquals(initialResources, roleRepository.count());
        }
    }

    @Test
    public void findByNameExistingNameTest(){
        Optional<Role> newRole = roleRepository.findByName("ROLE_USER");
        assertTrue(newRole.isPresent());
    }
    @Test
    public void findByNameNotExistingNameTest(){
        Optional<Role> newRole = roleRepository.findByName("hi");
        assertTrue(newRole.isEmpty());
    }
}
