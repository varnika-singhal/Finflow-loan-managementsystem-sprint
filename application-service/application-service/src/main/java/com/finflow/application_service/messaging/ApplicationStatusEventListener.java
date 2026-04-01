package com.finflow.application_service.messaging;

import com.finflow.application_service.config.RabbitMqConfig;
import com.finflow.application_service.entity.LoanApplication;
import com.finflow.application_service.repository.LoanApplicationRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class ApplicationStatusEventListener {

    private final LoanApplicationRepository repository;

    public ApplicationStatusEventListener(LoanApplicationRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = RabbitMqConfig.APPLICATION_DOCUMENT_STATUS_QUEUE)
    public void handleDocumentStatusChanged(DocumentStatusChangedEvent event) {
        if (event == null || event.getApplicationId() == null || event.getStatus() == null) {
            return;
        }

        repository.findById(event.getApplicationId()).ifPresent(application -> {
            application.setStatus(mapDocumentStatus(event.getStatus()));
            repository.save(application);
        });
    }

    @RabbitListener(queues = RabbitMqConfig.APPLICATION_DECISION_QUEUE)
    public void handleLoanDecision(LoanDecisionMadeEvent event) {
        if (event == null || event.getApplicationId() == null || event.getDecision() == null) {
            return;
        }

        repository.findById(event.getApplicationId()).ifPresent(application -> {
            application.setStatus(toTitleCase(event.getDecision()));
            repository.save(application);
        });
    }

    private String mapDocumentStatus(String documentStatus) {
        String normalizedStatus = documentStatus.trim().toUpperCase(Locale.ROOT);
        return switch (normalizedStatus) {
            case "VERIFIED" -> "Docs Verified";
            case "REJECTED" -> "Docs Pending";
            default -> "Docs Pending";
        };
    }

    private String toTitleCase(String value) {
        String normalized = value.trim().toLowerCase(Locale.ROOT);
        if (normalized.isEmpty()) {
            return value;
        }
        return Character.toUpperCase(normalized.charAt(0)) + normalized.substring(1);
    }
}
