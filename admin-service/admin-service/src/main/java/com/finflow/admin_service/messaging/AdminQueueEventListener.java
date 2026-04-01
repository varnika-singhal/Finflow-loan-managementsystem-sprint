package com.finflow.admin_service.messaging;

import com.finflow.admin_service.config.RabbitMqConfig;
import com.finflow.admin_service.entity.ApplicationQueueItem;
import com.finflow.admin_service.repository.ApplicationQueueItemRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Locale;

@Component
public class AdminQueueEventListener {

    private final ApplicationQueueItemRepository applicationQueueItemRepository;

    public AdminQueueEventListener(ApplicationQueueItemRepository applicationQueueItemRepository) {
        this.applicationQueueItemRepository = applicationQueueItemRepository;
    }

    @RabbitListener(queues = RabbitMqConfig.ADMIN_APPLICATION_SUBMITTED_QUEUE)
    public void handleApplicationSubmitted(ApplicationSubmittedEvent event) {
        if (event == null || event.getApplicationId() == null) {
            return;
        }

        ApplicationQueueItem queueItem = applicationQueueItemRepository.findByApplicationId(event.getApplicationId())
                .orElseGet(ApplicationQueueItem::new);

        LocalDateTime now = LocalDateTime.now();
        if (queueItem.getCreatedAt() == null) {
            queueItem.setCreatedAt(now);
        }

        queueItem.setApplicationId(event.getApplicationId());
        queueItem.setUserId(event.getUserId());
        queueItem.setApplicantName(event.getFullName());
        queueItem.setLoanType(event.getLoanType());
        queueItem.setLoanAmount(event.getLoanAmount());
        queueItem.setCurrentStatus(defaultValue(event.getStatus(), "Submitted"));
        queueItem.setLastEvent("APPLICATION_SUBMITTED");
        queueItem.setUpdatedAt(now);

        applicationQueueItemRepository.save(queueItem);
    }

    @RabbitListener(queues = RabbitMqConfig.ADMIN_DOCUMENT_STATUS_QUEUE)
    public void handleDocumentStatus(DocumentStatusChangedEvent event) {
        if (event == null || event.getApplicationId() == null || event.getStatus() == null) {
            return;
        }

        applicationQueueItemRepository.findByApplicationId(event.getApplicationId()).ifPresent(queueItem -> {
            queueItem.setCurrentStatus(mapDocumentStatus(event.getStatus()));
            queueItem.setLastEvent("DOCUMENT_" + event.getStatus().trim().toUpperCase(Locale.ROOT));
            queueItem.setUpdatedAt(LocalDateTime.now());
            applicationQueueItemRepository.save(queueItem);
        });
    }

    @RabbitListener(queues = RabbitMqConfig.ADMIN_DECISION_QUEUE)
    public void handleLoanDecision(LoanDecisionMadeEvent event) {
        if (event == null || event.getApplicationId() == null || event.getDecision() == null) {
            return;
        }

        applicationQueueItemRepository.findByApplicationId(event.getApplicationId()).ifPresent(queueItem -> {
            queueItem.setCurrentStatus(toTitleCase(event.getDecision()));
            queueItem.setLastEvent("DECISION_" + event.getDecision().trim().toUpperCase(Locale.ROOT));
            queueItem.setRemarks(event.getRemarks());
            queueItem.setUpdatedAt(LocalDateTime.now());
            applicationQueueItemRepository.save(queueItem);
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

    private String defaultValue(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
