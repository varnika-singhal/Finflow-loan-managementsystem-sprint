package com.finflow.admin_service.service;

import com.finflow.admin_service.entity.LoanDecision;
import com.finflow.admin_service.repository.LoanDecisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AdminService {

    @Autowired
    private LoanDecisionRepository repository;

    private RestTemplate restTemplate = new RestTemplate();

    // Save decision + update application status
    public LoanDecision makeDecision(LoanDecision decision) {

        LoanDecision saved = repository.save(decision);

        // Call Application Service
        String url = "http://localhost:8081/applications/"
                + decision.getApplicationId()
                + "/status?status=" + decision.getDecision();

        restTemplate.put(url, null);

        return saved;
    }

    // Get decision by applicationId
    public LoanDecision getDecision(Long applicationId) {
        return repository.findByApplicationId(applicationId)
                .orElseThrow(() -> new RuntimeException("Decision not found"));
    }
}