package com.sprk.security_demo_project.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;

    private String password;

    private String email;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.EAGER)
    @JoinTable(

            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<Role>();
}
