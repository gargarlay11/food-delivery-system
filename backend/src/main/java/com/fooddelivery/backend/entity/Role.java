package com.fooddelivery.backend.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    private Long id;

    @Column(
        nullable = false,
        unique = true,
        length = 50
    )
    private String name;

    @ManyToMany(
        mappedBy = "roles",
        fetch = FetchType.LAZY
    )
    private Set<User> users =
            new HashSet<>();

    public Role() {
    }

    public Role(
            Long id,
            String name
    ) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(
            Long id
    ) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(
            String name
    ) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(
            Set<User> users
    ) {
        this.users = users;
    }
}