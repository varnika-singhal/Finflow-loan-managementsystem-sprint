package com.finflow.authservice.controller;

import com.finflow.authservice.dto.*;
import jakarta.validation.Valid;
import com.finflow.authservice.service.AuthService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequestDTO dto) {
        return service.register(dto);
    }

    @PostMapping("/signup")
    public String signup(@Valid @RequestBody RegisterRequestDTO dto) {
        return service.register(dto);
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@Valid @RequestBody AuthRequestDTO dto) {
        return service.login(dto);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<?> getAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Object getUserById(@PathVariable Long id) {
        return service.getUserById(id);
    }

    @GetMapping("/users/by-username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public Object getUserByUsername(@PathVariable String username) {
        return service.getUserByUsername(username);
    }

    @GetMapping("/users/exists/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Boolean> userExists(@PathVariable String username) {
        return Map.of("exists", service.userExists(username));
    }

    @GetMapping("/users/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<?> getUsersByRole(@PathVariable String role) {
        return service.getUsersByRole(role);
    }

    @PutMapping("/users/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public Object updateUserRole(@PathVariable Long id, @RequestParam String role) {
        return service.updateUserRole(id, role);
    }

    @PutMapping("/users/{id}/password")
    @PreAuthorize("hasRole('ADMIN')")
    public Object updatePassword(@PathVariable Long id, @RequestParam String password) {
        return service.updatePassword(id, password);
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return Map.of("message", "User deleted");
    }
}
