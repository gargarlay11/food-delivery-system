package com.fooddelivery.backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.fooddelivery.backend.entity.Role;

public interface RoleRepository
        extends JpaRepository<Role, Long> {

    Optional<Role> findByName(
            String name
    );
}