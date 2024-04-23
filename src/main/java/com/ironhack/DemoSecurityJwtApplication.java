package com.ironhack;

import com.ironhack.demosecurityjwt.security.models.Role;
import com.ironhack.demosecurityjwt.security.models.User;
import com.ironhack.demosecurityjwt.security.services.impl.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class DemoSecurityJwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoSecurityJwtApplication.class, args);
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

            userService.saveUser(new User(null, "admin", "admin", "1234", new ArrayList<>(), null));
            userService.addRoleToUser("admin", "ROLE_ADMIN");
            userService.addRoleToUser("admin", "ROLE_USER");

            userService.saveUser(new User(null, "artist", "artist", "1234", new ArrayList<>(), null));
            userService.addRoleToUser("artist", "ROLE_ARTIST");
            userService.addRoleToUser("artist", "ROLE_USER");

            userService.saveUser(new User(null, "Julia", "ju", "1234", new ArrayList<>(), null));
            userService.addRoleToUser("ju", "ROLE_USER");

        };
    }

}
