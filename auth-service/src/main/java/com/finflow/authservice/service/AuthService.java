package com.finflow.authservice.service;


import com.finflow.authservice.dto.*;
import com.finflow.authservice.entity.User;
import com.finflow.authservice.repository.UserRepository;
import com.finflow.authservice.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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

        String token = jwtUtil.generateToken(user);

        return new AuthResponseDTO(token, "Login successful", user.getId(), user.getRole());
    }

    public List<User> getAllUsers() {
        return repo.findAll();
    }

    public User getUserById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public User getUserByUsername(String username) {
        return repo.findTopByUsernameOrderByIdDesc(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public boolean userExists(String username) {
        return repo.existsByUsername(username);
    }

    public List<User> getUsersByRole(String role) {
        return repo.findByRole(resolveRole(role));
    }

    public User updateUserRole(Long id, String role) {
        User user = getUserById(id);
        user.setRole(resolveRole(role));
        return repo.save(user);
    }

    public User updatePassword(Long id, String password) {
        User user = getUserById(id);
        user.setPassword(password);
        return repo.save(user);
    }

    public void deleteUser(Long id) {
        User user = getUserById(id);
        repo.delete(user);
    }

    private String resolveRole(String role) {
        if (role == null || role.isBlank()) {
            return "APPLICANT";
        }

        return role.trim().toUpperCase(Locale.ROOT);
    }
}
