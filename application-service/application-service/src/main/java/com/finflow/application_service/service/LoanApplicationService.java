package com.finflow.application_service.service;

import com.finflow.application_service.entity.LoanApplication;
import com.finflow.application_service.repository.LoanApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
public class LoanApplicationService {

    private final LoanApplicationRepository repository;

    public LoanApplicationService(LoanApplicationRepository repository) {
        this.repository = repository;
    }

    public LoanApplication createApplication(LoanApplication app) {
        app.setStatus("Draft");
        return repository.save(app);
    }

    public LoanApplication submitApplication(Long id) {
        LoanApplication app = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found"));

        app.setStatus("Submitted");
        return repository.save(app);
    }

    public List<LoanApplication> getUserApplications(Long userId) {
        return repository.findByUserId(userId);
    }

    public LoanApplication updateApplication(Long id, LoanApplication updated) {

        LoanApplication existing = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found"));

        existing.setFullName(updated.getFullName());
        existing.setEmploymentStatus(updated.getEmploymentStatus());
        existing.setAnnualIncome(updated.getAnnualIncome());
        existing.setLoanAmount(updated.getLoanAmount());
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
}
