package com.finflow.application_service.messaging;

import com.finflow.application_service.config.RabbitMqConfig;
import com.finflow.application_service.entity.LoanApplication;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ApplicationEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public ApplicationEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishApplicationSubmitted(LoanApplication application) {
        ApplicationSubmittedEvent event = new ApplicationSubmittedEvent();
        event.setApplicationId(application.getId());
        event.setUserId(application.getUserId());
        event.setFullName(application.getFullName());
        event.setLoanType(application.getLoanType());
        event.setLoanAmount(application.getLoanAmount());
        event.setStatus(application.getStatus());
        event.setSubmittedAt(LocalDateTime.now());

        rabbitTemplate.convertAndSend(
                RabbitMqConfig.EVENTS_EXCHANGE,
                RabbitMqConfig.APPLICATION_SUBMITTED_ROUTING_KEY,
                event
        );
    }
}
