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

        // 1. Authenticate user (checks email + password)
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        // 2. Get UserDetails from authentication
        org.springframework.security.core.userdetails.UserDetails userDetails = 
                (org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal();

        // 3. Fetch full user from DB for response
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 4. Generate JWT token
        String token = jwtService.generateToken(userDetails);

        // 5. Create response DTO
        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone()
        );

        // 6. Return token + user info
        return new AuthResponse(token, userResponse);
    }
}