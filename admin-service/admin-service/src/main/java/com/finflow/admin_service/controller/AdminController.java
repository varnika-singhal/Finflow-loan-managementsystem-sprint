package com.finflow.admin_service.controller;

import com.finflow.admin_service.entity.LoanDecision;
import com.finflow.admin_service.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService service;

    // Approve / Reject
    @PostMapping("/decision")
    public LoanDecision makeDecision(@RequestBody LoanDecision decision) {
        return service.makeDecision(decision);
    }

    // Get decision
    @GetMapping("/{applicationId}")
    public LoanDecision getDecision(@PathVariable Long applicationId) {
        return service.getDecision(applicationId);
    }
}