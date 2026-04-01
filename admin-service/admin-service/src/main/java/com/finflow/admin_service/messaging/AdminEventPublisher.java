package com.finflow.admin_service.messaging;

import com.finflow.admin_service.config.RabbitMqConfig;
import com.finflow.admin_service.entity.LoanDecision;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AdminEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public AdminEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishLoanDecisionMade(LoanDecision loanDecision) {
        LoanDecisionMadeEvent event = new LoanDecisionMadeEvent();
        event.setApplicationId(loanDecision.getApplicationId());
        event.setDecision(loanDecision.getDecision());
        event.setRemarks(loanDecision.getRemarks());
        event.setDecidedAt(LocalDateTime.now());

        rabbitTemplate.convertAndSend(
                RabbitMqConfig.EVENTS_EXCHANGE,
                RabbitMqConfig.DECISION_ROUTING_KEY,
                event
        );
    }
}
