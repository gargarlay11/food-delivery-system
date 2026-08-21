package com.fooddelivery.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fooddelivery.backend.entity.User;

public interface UserRepository
        extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmailIgnoreCase(
            String email
    );

    Optional<User> findByUsernameIgnoreCase(
            String username
    );

    boolean existsByEmailIgnoreCase(
            String email
    );

    boolean existsByUsernameIgnoreCase(
            String username
    );
}