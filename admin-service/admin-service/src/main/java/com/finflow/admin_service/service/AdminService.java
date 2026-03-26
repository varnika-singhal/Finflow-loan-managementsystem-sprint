package com.finflow.admin_service.service;

import com.finflow.admin_service.entity.LoanDecision;
import com.finflow.admin_service.repository.LoanDecisionRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AdminService {

    private final LoanDecisionRepository repository;
    private final RestTemplate restTemplate;

    public AdminService(LoanDecisionRepository repository) {
        this.repository = repository;
        this.restTemplate = new RestTemplate();
    }

    // Save decision + update application status
    public LoanDecision makeDecision(LoanDecision decision, String authorizationHeader) {

        LoanDecision saved = repository.save(decision);

        // Call Application Service
        String url = "http://localhost:8081/applications/"
                + decision.getApplicationId()
                + "/status?status=" + decision.getDecision();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, authorizationHeader);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);

        return saved;
    }

    // Get decision by applicationId
    public LoanDecision getDecision(Long applicationId) {
        return repository.findTopByApplicationIdOrderByIdDesc(applicationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Decision not found"));
    }
}
