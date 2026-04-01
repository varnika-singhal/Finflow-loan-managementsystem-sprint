package com.finflow.application_service.service;

import com.finflow.application_service.entity.LoanApplication;
import com.finflow.application_service.messaging.ApplicationEventPublisher;
import com.finflow.application_service.repository.LoanApplicationRepository;
import com.finflow.application_service.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
@Service
public class LoanApplicationService {

    private final LoanApplicationRepository repository;
    private final JwtUtil jwtUtil;
    private final ApplicationEventPublisher applicationEventPublisher;

    public LoanApplicationService(LoanApplicationRepository repository,
                                  JwtUtil jwtUtil,
                                  ApplicationEventPublisher applicationEventPublisher) {
        this.repository = repository;
        this.jwtUtil = jwtUtil;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public LoanApplication createApplication(LoanApplication app) {
        app.setStatus("Draft");
        return repository.save(app);
    }

    public List<LoanApplication> getAllApplications() {
        return repository.findAll();
    }

    public LoanApplication submitApplication(Long id) {
        LoanApplication app = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found"));

        app.setStatus("Submitted");
        LoanApplication saved = repository.save(app);
        applicationEventPublisher.publishApplicationSubmitted(saved);
        return saved;
    }

    public List<LoanApplication> getUserApplications(Long userId) {
        return repository.findByUserId(userId);
    }

    public List<LoanApplication> getMyApplications(String authorizationHeader) {
        Long userId = extractUserId(authorizationHeader);
        return repository.findByUserId(userId);
    }

    public List<LoanApplication> getApplicationsByStatus(String status) {
        return repository.findByStatus(status);
    }

    public List<LoanApplication> getApplicationsByLoanType(String loanType) {
        return repository.findByLoanType(loanType);
    }

    public LoanApplication updateApplication(Long id, LoanApplication updated) {

        LoanApplication existing = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found"));

        existing.setFullName(updated.getFullName());
        existing.setEmploymentStatus(updated.getEmploymentStatus());
        existing.setAnnualIncome(updated.getAnnualIncome());
        existing.setLoanAmount(updated.getLoanAmount());
        existing.setLoanType(updated.getLoanType());
        existing.setTenure(updated.getTenure());

        return repository.save(existing);
    }

    // ✅ ADD THIS METHOD (IMPORTANT)
    public LoanApplication getApplicationById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found"));
    }

    public String getApplicationStatus(Long id) {
        LoanApplication app = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found"));

        return app.getStatus();
    }

    public LoanApplication updateStatus(Long id, String status) {

        LoanApplication app = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found"));

        app.setStatus(status);
        return repository.save(app);
    }

    public LoanApplication markDraft(Long id) {
        return updateStatus(id, "Draft");
    }

    public LoanApplication approve(Long id) {
        return updateStatus(id, "Approved");
    }

    public LoanApplication reject(Long id) {
        return updateStatus(id, "Rejected");
    }

    public LoanApplication close(Long id) {
        return updateStatus(id, "Closed");
    }

    public LoanApplication updateLoanType(Long id, String loanType) {
        LoanApplication app = getApplicationById(id);
        app.setLoanType(loanType);
        return repository.save(app);
    }

    public void deleteApplication(Long id) {
        LoanApplication app = getApplicationById(id);
        repository.delete(app);
    }

    public long getApplicationCountByLoanType(String loanType) {
        return repository.countByLoanType(loanType);
    }

    private Long extractUserId(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header");
        }

        Long userId = jwtUtil.extractUserId(authorizationHeader.substring(7));
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User id missing in JWT token");
        }
        return userId;
    }
}
