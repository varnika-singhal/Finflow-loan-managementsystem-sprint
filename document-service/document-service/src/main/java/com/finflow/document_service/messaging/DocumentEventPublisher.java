package com.finflow.document_service.messaging;

import com.finflow.document_service.config.RabbitMqConfig;
import com.finflow.document_service.entity.Document;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DocumentEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public DocumentEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishDocumentStatusChanged(Document document) {
        DocumentStatusChangedEvent event = new DocumentStatusChangedEvent();
        event.setDocumentId(document.getId());
        event.setApplicationId(document.getApplicationId());
        event.setDocumentType(document.getDocumentType());
        event.setStatus(document.getStatus());
        event.setChangedAt(LocalDateTime.now());

        rabbitTemplate.convertAndSend(
                RabbitMqConfig.EVENTS_EXCHANGE,
                RabbitMqConfig.DOCUMENT_STATUS_ROUTING_KEY,
                event
        );
    }
}
