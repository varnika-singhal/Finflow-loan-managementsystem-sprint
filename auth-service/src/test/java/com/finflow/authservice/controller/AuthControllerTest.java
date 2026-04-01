package com.finflow.authservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finflow.authservice.config.JwtAuthenticationFilter;
import com.finflow.authservice.dto.AuthRequestDTO;
import com.finflow.authservice.dto.AuthResponseDTO;
import com.finflow.authservice.dto.RegisterRequestDTO;
import com.finflow.authservice.service.AuthService;
import com.finflow.authservice.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void registerShouldReturnSuccessMessage() throws Exception {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setUsername("varnikas");
        dto.setPassword("123456");
        dto.setRole("APPLICANT");

        when(authService.register(any(RegisterRequestDTO.class))).thenReturn("User Registered");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void loginShouldReturnJwtResponse() throws Exception {
        AuthRequestDTO dto = new AuthRequestDTO();
        dto.setUsername("varnikas");
        dto.setPassword("123456");

        AuthResponseDTO response = new AuthResponseDTO("jwt-token", "Login successful", 7L, "APPLICANT");
        when(authService.login(any(AuthRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.userId").value(7))
                .andExpect(jsonPath("$.role").value("APPLICANT"));
    }
}
