package com.ironhack;

import com.ironhack.security.utils.ArtistStatus;
import com.ironhack.security.model.Role;
import com.ironhack.security.model.User;
import com.ironhack.security.service.impl.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class IronMusicApplication {

    public static void main(String[] args) {
        SpringApplication.run(IronMusicApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    CommandLineRunner run(UserService userService) {
        return args -> {
            userService.saveRole(new Role(null, "ROLE_USER"));
            userService.saveRole(new Role(null, "ROLE_ADMIN"));
            userService.saveRole(new Role(null, "ROLE_ARTIST"));

            User user = userService.saveUser(new User(null, "admin", "admin", "1234",
                    true, ArtistStatus.ACTIVE, new ArrayList<>(), null));
            userService.activeAllUsers();
            userService.addRoleToUser("admin", "ROLE_ADMIN");
            userService.addRoleToUser("admin", "ROLE_USER");
            userService.addRoleToUser("admin", "ROLE_ARTIST");
        };
    }

}
