package com.finflow.admin_service.controller;

import com.finflow.admin_service.dto.AdminDecisionRequest;
import com.finflow.admin_service.dto.DocumentResponse;
import com.finflow.admin_service.dto.UserUpdateRequest;
import com.finflow.admin_service.entity.ApplicationQueueItem;
import com.finflow.admin_service.entity.LoanDecision;
import com.finflow.admin_service.entity.Report;
import com.finflow.admin_service.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService service;

    public AdminController(AdminService service) {
        this.service = service;
    }

    // Approve / Reject
    @PostMapping("/decision")
    public LoanDecision makeDecision(
            @Valid @RequestBody LoanDecision decision,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        return service.makeDecision(decision, authorizationHeader);
    }

    @PostMapping("/applications/{id}/decision")
    public LoanDecision makeDecisionForApplication(
            @PathVariable Long id,
            @Valid @RequestBody AdminDecisionRequest request,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        return service.makeDecision(id, request, authorizationHeader);
    }

    // Get decision
    @GetMapping("/{applicationId}")
    public LoanDecision getDecision(@PathVariable Long applicationId) {
        return service.getDecision(applicationId);
    }

    @GetMapping("/applications")
    public List<ApplicationQueueItem> getApplicationQueue(@RequestHeader("Authorization") String authorizationHeader) {
        return service.getApplicationQueue(authorizationHeader);
    }

    @PutMapping("/documents/{id}/verify")
    public DocumentResponse verifyDocument(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        return service.verifyDocument(id, authorizationHeader);
    }

    @GetMapping("/decisions")
    public List<LoanDecision> getAllDecisions() {
        return service.getAllDecisions();
    }

    @GetMapping("/decisions/id/{id}")
    public LoanDecision getDecisionById(@PathVariable Long id) {
        return service.getDecisionById(id);
    }

    @GetMapping("/decisions/status/{decision}")
    public List<LoanDecision> getDecisionsByStatus(@PathVariable String decision) {
        return service.getDecisionsByStatus(decision);
    }

    @DeleteMapping("/decisions/{id}")
    public Map<String, String> deleteDecision(@PathVariable Long id) {
        service.deleteDecision(id);
        return Map.of("message", "Decision deleted");
    }

    @GetMapping("/reports")
    public Report generateReport(@RequestHeader("Authorization") String authorizationHeader) {
        return service.generateReport(authorizationHeader);
    }

    @GetMapping("/users")
    public List<?> getUsers(@RequestHeader("Authorization") String authorizationHeader) {
        return service.getUsers(authorizationHeader);
    }

    @PutMapping("/users/{id}")
    public Object updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateRequest request,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        return service.updateUser(id, request, authorizationHeader);
    }
}
