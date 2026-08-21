package com.fooddelivery.backend.service.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fooddelivery.backend.dto.AuthResponse;
import com.fooddelivery.backend.dto.LoginRequest;
import com.fooddelivery.backend.dto.RegisterRequest;
import com.fooddelivery.backend.entity.Role;
import com.fooddelivery.backend.entity.User;
import com.fooddelivery.backend.exception.InvalidCredentialsException;
import com.fooddelivery.backend.exception.RoleNotFoundException;
import com.fooddelivery.backend.exception.UserAlreadyExistsException;
import com.fooddelivery.backend.repository.RoleRepository;
import com.fooddelivery.backend.repository.UserRepository;
import com.fooddelivery.backend.security.CustomUserDetailsService;
import com.fooddelivery.backend.security.JwtService;
import com.fooddelivery.backend.service.AuthService;

@Service
@Transactional
public class AuthServiceImpl
        implements AuthService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager
            authenticationManager;

    private final CustomUserDetailsService
            userDetailsService;

    private final JwtService jwtService;

    public AuthServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            CustomUserDetailsService userDetailsService,
            JwtService jwtService
    ) {

        this.userRepository =
                userRepository;

        this.roleRepository =
                roleRepository;

        this.passwordEncoder =
                passwordEncoder;

        this.authenticationManager =
                authenticationManager;

        this.userDetailsService =
                userDetailsService;

        this.jwtService =
                jwtService;
    }

    @Override
    public AuthResponse register(
            RegisterRequest request
    ) {

        String username =
                request.getUsername()
                    .trim();

        String email =
                request.getEmail()
                    .trim()
                    .toLowerCase();

        if (
            userRepository
                .existsByUsernameIgnoreCase(
                    username
                )
        ) {
            throw new UserAlreadyExistsException(
                "Username already exists"
            );
        }

        if (
            userRepository
                .existsByEmailIgnoreCase(
                    email
                )
        ) {
            throw new UserAlreadyExistsException(
                "Email already exists"
            );
        }

        Role customerRole =
                roleRepository
                    .findByName(
                        "ROLE_CUSTOMER"
                    )
                    .orElseThrow(
                        () ->
                            new RoleNotFoundException(
                                "ROLE_CUSTOMER"
                            )
                    );

        User user =
                new User();

        user.setUsername(
                username
        );

        user.setEmail(
                email
        );

        user.setPassword(
                passwordEncoder.encode(
                    request.getPassword()
                )
        );

        user.setEnabled(true);

        Set<Role> roles =
                new HashSet<>();

        roles.add(
            customerRole
        );

        user.setRoles(
                roles
        );

        User savedUser =
                userRepository.save(
                    user
                );

        UserDetails userDetails =
                userDetailsService
                    .loadUserByUsername(
                        savedUser.getEmail()
                    );

        String token =
                jwtService.generateToken(
                    userDetails
                );

        return buildAuthResponse(
                savedUser,
                token
        );
    }

    @Override
    public AuthResponse login(
            LoginRequest request
    ) {

        String email =
                request.getEmail()
                    .trim()
                    .toLowerCase();

        try {

            authenticationManager
                .authenticate(
                    new UsernamePasswordAuthenticationToken(
                        email,
                        request.getPassword()
                    )
                );

        } catch (
            BadCredentialsException exception
        ) {

            throw new InvalidCredentialsException();
        }

        User user =
                userRepository
                    .findByEmailIgnoreCase(
                        email
                    )
                    .orElseThrow(
                        InvalidCredentialsException::new
                    );

        UserDetails userDetails =
                userDetailsService
                    .loadUserByUsername(
                        email
                    );

        String token =
                jwtService.generateToken(
                    userDetails
                );

        return buildAuthResponse(
                user,
                token
        );
    }

    private AuthResponse buildAuthResponse(
            User user,
            String token
    ) {

        Set<String> roles =
                user.getRoles()
                    .stream()
                    .map(
                        Role::getName
                    )
                    .collect(
                        Collectors.toSet()
                    );

        return new AuthResponse(
                token,
                "Bearer",
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roles
        );
    }
}