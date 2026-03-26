package com.finflow.application_service.controller;

import com.finflow.application_service.entity.LoanApplication;
import com.finflow.application_service.service.LoanApplicationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applications")
public class LoanApplicationController {

    private final LoanApplicationService service;

    public LoanApplicationController(LoanApplicationService service) {
        this.service = service;
    }

    // Create Application
    @PostMapping
    public LoanApplication create(@Valid @RequestBody LoanApplication app) {
        return service.createApplication(app);
    }

    // Submit Application
    @PostMapping("/{id}/submit")
    public LoanApplication submit(@PathVariable Long id) {
        return service.submitApplication(id);
    }

    @PutMapping("/{id}")
    public LoanApplication update(@PathVariable Long id, @Valid @RequestBody LoanApplication app) {
        return service.updateApplication(id, app);
    }

    // Get Applications by User
    @GetMapping("/user/{userId}")
    public List<LoanApplication> getByUser(@PathVariable Long userId) {
        return service.getUserApplications(userId);
    }
    @GetMapping("/{id}/status")
    public String getStatus(@PathVariable Long id) {
        return service.getApplicationStatus(id);
    }

    @PutMapping("/{id}/status")
    public LoanApplication updateStatus(@PathVariable Long id, @RequestParam String status) {
        return service.updateStatus(id, status);
    }

    @GetMapping("/{id}")
    public LoanApplication getApplication(@PathVariable Long id) {
        return service.getApplicationById(id);
    }
}
