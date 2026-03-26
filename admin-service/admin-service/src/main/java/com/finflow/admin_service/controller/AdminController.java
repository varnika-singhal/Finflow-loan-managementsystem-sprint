package com.finflow.admin_service.controller;

import com.finflow.admin_service.entity.LoanDecision;
import com.finflow.admin_service.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
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

    // Get decision
    @GetMapping("/{applicationId}")
    public LoanDecision getDecision(@PathVariable Long applicationId) {
        return service.getDecision(applicationId);
    }
}
