package com.finflow.application_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finflow.application_service.config.JwtAuthenticationFilter;
import com.finflow.application_service.entity.LoanApplication;
import com.finflow.application_service.service.LoanApplicationService;
import com.finflow.application_service.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoanApplicationController.class)
@AutoConfigureMockMvc(addFilters = false)
class LoanApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoanApplicationService loanApplicationService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void createShouldReturnCreatedLoanApplication() throws Exception {
        LoanApplication application = buildApplication();
        application.setId(5L);
        application.setStatus("Draft");

        when(loanApplicationService.createApplication(any(LoanApplication.class))).thenReturn(application);

        mockMvc.perform(post("/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildApplication())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.status").value("Draft"));
    }

    @Test
    void getByLoanTypeShouldReturnMatchingApplications() throws Exception {
        LoanApplication application = buildApplication();
        application.setId(5L);
        application.setStatus("Draft");

        when(loanApplicationService.getApplicationsByLoanType("Home Loan"))
                .thenReturn(List.of(application));

        mockMvc.perform(get("/applications/loan-type/Home Loan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].loanType").value("Home Loan"))
                .andExpect(jsonPath("$[0].fullName").value("Varnikas"));
    }

    @Test
    void deleteShouldReturnSuccessMessage() throws Exception {
        doNothing().when(loanApplicationService).deleteApplication(eq(5L));

        mockMvc.perform(delete("/applications/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Application deleted"));
    }

    private LoanApplication buildApplication() {
        LoanApplication application = new LoanApplication();
        application.setUserId(7L);
        application.setFullName("Varnikas");
        application.setEmploymentStatus("Salaried");
        application.setAnnualIncome(600000.0);
        application.setLoanAmount(300000.0);
        application.setLoanType("Home Loan");
        application.setTenure(24);
        return application;
    }
}
