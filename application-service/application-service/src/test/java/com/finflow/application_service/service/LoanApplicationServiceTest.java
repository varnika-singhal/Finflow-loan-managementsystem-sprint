package com.finflow.application_service.service;

import com.finflow.application_service.entity.LoanApplication;
import com.finflow.application_service.messaging.ApplicationEventPublisher;
import com.finflow.application_service.repository.LoanApplicationRepository;
import com.finflow.application_service.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanApplicationServiceTest {

    @Mock
    private LoanApplicationRepository repository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private LoanApplicationService loanApplicationService;

    @Test
    void createApplicationShouldSetDraftStatusBeforeSaving() {
        LoanApplication application = buildApplication();
        when(repository.save(any(LoanApplication.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LoanApplication saved = loanApplicationService.createApplication(application);

        assertEquals("Draft", saved.getStatus());
        verify(repository).save(application);
    }

    @Test
    void getMyApplicationsShouldReturnApplicationsForUserFromBearerToken() {
        when(jwtUtil.extractUserId("valid-token")).thenReturn(7L);
        when(repository.findByUserId(7L)).thenReturn(List.of(buildApplication()));

        List<LoanApplication> result = loanApplicationService.getMyApplications("Bearer valid-token");

        assertEquals(1, result.size());
        verify(repository).findByUserId(7L);
    }

    @Test
    void submitApplicationShouldThrowUnauthorizedForMissingBearerHeader() {
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> loanApplicationService.getMyApplications("invalid-header")
        );

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Missing or invalid Authorization header", exception.getReason());
    }

    @Test
    void submitApplicationShouldUpdateStatusToSubmitted() {
        LoanApplication application = buildApplication();
        application.setId(5L);
        application.setStatus("Draft");

        when(repository.findById(5L)).thenReturn(Optional.of(application));
        when(repository.save(any(LoanApplication.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LoanApplication updated = loanApplicationService.submitApplication(5L);

        assertEquals("Submitted", updated.getStatus());
        verify(applicationEventPublisher).publishApplicationSubmitted(updated);
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
