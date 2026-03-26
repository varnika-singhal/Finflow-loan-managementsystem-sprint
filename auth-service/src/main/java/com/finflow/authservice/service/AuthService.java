package com.finflow.authservice.service;


import com.finflow.authservice.dto.*;
import com.finflow.authservice.entity.User;
import com.finflow.authservice.repository.UserRepository;
import com.finflow.authservice.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
public class AuthService {

    private final UserRepository repo;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository repo, JwtUtil jwtUtil) {
        this.repo = repo;
        this.jwtUtil = jwtUtil;
    }

    public String register(RegisterRequestDTO dto) {
        if (repo.existsByUsername(dto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRole(resolveRole(dto.getRole()));

        repo.save(user);
        return "User Registered";
    }

    public AuthResponseDTO login(AuthRequestDTO dto) {
        User user = repo.findTopByUsernameOrderByIdDesc(dto.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!user.getPassword().equals(dto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }

        String token = jwtUtil.generateToken(user.getUsername());

        return new AuthResponseDTO(token, "Login successful");
    }

    private String resolveRole(String role) {
        if (role == null || role.isBlank()) {
            return "APPLICANT";
        }

        return role.trim().toUpperCase(Locale.ROOT);
    }
}
