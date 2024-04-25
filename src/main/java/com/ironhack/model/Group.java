package com.ironhack.model;

import com.ironhack.demosecurityjwt.security.models.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotEmpty(message = "Bad request. Group name is required.")
    private String name;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "groups_users", //join table name
            joinColumns = @JoinColumn(name = "group_id"), // ref column this entity
            inverseJoinColumns = @JoinColumn(name = "user_id") // audio ref column
    )
    private List<User> users; //TODO: pending to add groups to USER

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Playlist> playlists;

}
