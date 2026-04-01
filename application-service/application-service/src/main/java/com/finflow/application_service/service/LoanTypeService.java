package com.finflow.application_service.service;

import com.finflow.application_service.entity.LoanType;
import com.finflow.application_service.repository.LoanTypeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class LoanTypeService {

    private final LoanTypeRepository repository;

    public LoanTypeService(LoanTypeRepository repository) {
        this.repository = repository;
    }

    public LoanType createLoanType(LoanType loanType) {
        loanType.setActive(true);
        return repository.save(loanType);
    }

    public List<LoanType> getAllLoanTypes() {
        return repository.findAll();
    }

    public LoanType getLoanTypeById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan type not found"));
    }

    public LoanType getLoanTypeByName(String name) {
        return repository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan type not found"));
    }

    public List<LoanType> getActiveLoanTypes() {
        return repository.findByActiveTrue();
    }

    public LoanType updateLoanType(Long id, LoanType updatedLoanType) {
        LoanType existingLoanType = getLoanTypeById(id);
        existingLoanType.setName(updatedLoanType.getName());
        existingLoanType.setDescription(updatedLoanType.getDescription());
        existingLoanType.setMinAmount(updatedLoanType.getMinAmount());
        existingLoanType.setMaxAmount(updatedLoanType.getMaxAmount());
        existingLoanType.setMinTenure(updatedLoanType.getMinTenure());
        existingLoanType.setMaxTenure(updatedLoanType.getMaxTenure());
        return repository.save(existingLoanType);
    }

    public LoanType activateLoanType(Long id) {
        LoanType loanType = getLoanTypeById(id);
        loanType.setActive(true);
        return repository.save(loanType);
    }

    public LoanType deactivateLoanType(Long id) {
        LoanType loanType = getLoanTypeById(id);
        loanType.setActive(false);
        return repository.save(loanType);
    }

    public void deleteLoanType(Long id) {
        LoanType loanType = getLoanTypeById(id);
        repository.delete(loanType);
    }
}
