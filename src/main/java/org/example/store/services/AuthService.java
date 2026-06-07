package org.example.store.services;

import lombok.RequiredArgsConstructor;
import org.example.store.dto.AuthResponse;
import org.example.store.dto.LoginRequest;
import org.example.store.dto.UserResponse;
import org.example.store.entities.User;
import org.example.store.repositories.UserRepository;
import org.example.store.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthResponse login(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user);

        UserResponse userResponse;
        userResponse = new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone()
        );

        return new AuthResponse(token, userResponse);
    }
}