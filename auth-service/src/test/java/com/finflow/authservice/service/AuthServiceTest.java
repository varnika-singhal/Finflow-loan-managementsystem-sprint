package com.finflow.authservice.service;

import com.finflow.authservice.dto.AuthRequestDTO;
import com.finflow.authservice.dto.AuthResponseDTO;
import com.finflow.authservice.dto.RegisterRequestDTO;
import com.finflow.authservice.entity.User;
import com.finflow.authservice.repository.UserRepository;
import com.finflow.authservice.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository repo;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerShouldDefaultRoleToApplicantWhenRoleIsBlank() {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setUsername("varnika");
        dto.setPassword("123456");
        dto.setRole(" ");
        when(repo.existsByUsername("varnika")).thenReturn(false);

        String result = authService.register(dto);

        assertEquals("User Registered", result);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(repo).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals("varnika", savedUser.getUsername());
        assertEquals("123456", savedUser.getPassword());
        assertEquals("APPLICANT", savedUser.getRole());
    }

    @Test
    void registerShouldThrowConflictWhenUsernameAlreadyExists() {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setUsername("varnika");
        dto.setPassword("123456");
        dto.setRole("APPLICANT");
        when(repo.existsByUsername("varnika")).thenReturn(true);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> authService.register(dto)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Username already exists", exception.getReason());
    }

    @Test
    void loginShouldReturnTokenAndUserDetailsForValidCredentials() {
        User user = new User(7L, "varnikas", "123456", "APPLICANT");
        AuthRequestDTO dto = new AuthRequestDTO();
        dto.setUsername("varnikas");
        dto.setPassword("123456");

        when(repo.findTopByUsernameOrderByIdDesc("varnikas")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(user)).thenReturn("jwt-token");

        AuthResponseDTO response = authService.login(dto);

        assertEquals("jwt-token", response.getToken());
        assertEquals("Login successful", response.getMessage());
        assertEquals(7L, response.getUserId());
        assertEquals("APPLICANT", response.getRole());
    }
}
