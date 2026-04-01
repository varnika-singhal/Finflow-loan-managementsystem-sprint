package com.finflow.application_service.controller;

import com.finflow.application_service.entity.LoanType;
import com.finflow.application_service.service.LoanTypeService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/loan-types")
public class LoanTypeController {

    private final LoanTypeService service;

    public LoanTypeController(LoanTypeService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public LoanType createLoanType(@Valid @RequestBody LoanType loanType) {
        return service.createLoanType(loanType);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public List<LoanType> getAllLoanTypes() {
        return service.getAllLoanTypes();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public LoanType getLoanTypeById(@PathVariable Long id) {
        return service.getLoanTypeById(id);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public LoanType getLoanTypeByName(@PathVariable String name) {
        return service.getLoanTypeByName(name);
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public List<LoanType> getActiveLoanTypes() {
        return service.getActiveLoanTypes();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public LoanType updateLoanType(@PathVariable Long id, @Valid @RequestBody LoanType loanType) {
        return service.updateLoanType(id, loanType);
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public LoanType activateLoanType(@PathVariable Long id) {
        return service.activateLoanType(id);
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public LoanType deactivateLoanType(@PathVariable Long id) {
        return service.deactivateLoanType(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> deleteLoanType(@PathVariable Long id) {
        service.deleteLoanType(id);
        return Map.of("message", "Loan type deleted");
    }
}
