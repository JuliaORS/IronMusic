package com.ironhack.model;

import com.ironhack.demosecurityjwt.security.models.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
