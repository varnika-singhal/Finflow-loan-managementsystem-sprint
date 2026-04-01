package com.finflow.application_service.controller;

import com.finflow.application_service.entity.LoanType;
import com.finflow.application_service.service.LoanTypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    private final LoanTypeService loanTypeService;

    public ProductController(LoanTypeService loanTypeService) {
        this.loanTypeService = loanTypeService;
    }

    @GetMapping("/products")
    public List<LoanType> getProducts() {
        return loanTypeService.getActiveLoanTypes();
    }
}
