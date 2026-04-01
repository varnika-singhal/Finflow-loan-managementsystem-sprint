package com.finflow.application_service.controller;

import com.finflow.application_service.entity.LoanApplication;
import com.finflow.application_service.service.LoanApplicationService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/applications")
public class LoanApplicationController {

    private final LoanApplicationService service;

    public LoanApplicationController(LoanApplicationService service) {
        this.service = service;
    }

    // Create Application
    @PostMapping
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public LoanApplication create(@Valid @RequestBody LoanApplication app) {
        return service.createApplication(app);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<LoanApplication> getAllApplications() {
        return service.getAllApplications();
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public List<LoanApplication> getMyApplications(@RequestHeader("Authorization") String authorizationHeader) {
        return service.getMyApplications(authorizationHeader);
    }

    // Submit Application
    @PostMapping("/{id}/submit")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public LoanApplication submit(@PathVariable Long id) {
        return service.submitApplication(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public LoanApplication update(@PathVariable Long id, @Valid @RequestBody LoanApplication app) {
        return service.updateApplication(id, app);
    }

    // Get Applications by User
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<LoanApplication> getByUser(@PathVariable Long userId) {
        return service.getUserApplications(userId);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<LoanApplication> getByStatus(@PathVariable String status) {
        return service.getApplicationsByStatus(status);
    }

    @GetMapping("/loan-type/{loanType}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public List<LoanApplication> getByLoanType(@PathVariable String loanType) {
        return service.getApplicationsByLoanType(loanType);
    }

    @GetMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public String getStatus(@PathVariable Long id) {
        return service.getApplicationStatus(id);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public LoanApplication updateStatus(@PathVariable Long id, @RequestParam String status) {
        return service.updateStatus(id, status);
    }

    @PutMapping("/{id}/draft")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public LoanApplication markDraft(@PathVariable Long id) {
        return service.markDraft(id);
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public LoanApplication approve(@PathVariable Long id) {
        return service.approve(id);
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public LoanApplication reject(@PathVariable Long id) {
        return service.reject(id);
    }

    @PutMapping("/{id}/close")
    @PreAuthorize("hasRole('ADMIN')")
    public LoanApplication close(@PathVariable Long id) {
        return service.close(id);
    }

    @PutMapping("/{id}/loan-type")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public LoanApplication updateLoanType(@PathVariable Long id, @RequestParam String loanType) {
        return service.updateLoanType(id, loanType);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public LoanApplication getApplication(@PathVariable Long id) {
        return service.getApplicationById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> deleteApplication(@PathVariable Long id) {
        service.deleteApplication(id);
        return Map.of("message", "Application deleted");
    }

    @GetMapping("/count/loan-type/{loanType}")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Long> getApplicationCountByLoanType(@PathVariable String loanType) {
        return Map.of("count", service.getApplicationCountByLoanType(loanType));
    }
}
