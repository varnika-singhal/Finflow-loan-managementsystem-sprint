package com.finflow.admin_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String EVENTS_EXCHANGE = "finflow.events.exchange";
    public static final String ADMIN_APPLICATION_SUBMITTED_QUEUE = "finflow.admin.application-submitted.queue";
    public static final String ADMIN_DOCUMENT_STATUS_QUEUE = "finflow.admin.document-status.queue";
    public static final String ADMIN_DECISION_QUEUE = "finflow.admin.decision.queue";
    public static final String APPLICATION_SUBMITTED_ROUTING_KEY = "loan.application.submitted";
    public static final String DOCUMENT_STATUS_ROUTING_KEY = "loan.document.status.changed";
    public static final String DECISION_ROUTING_KEY = "loan.decision.made";

    @Bean
    DirectExchange finflowEventsExchange() {
        return new DirectExchange(EVENTS_EXCHANGE, true, false);
    }

    @Bean
    Queue adminApplicationSubmittedQueue() {
        return new Queue(ADMIN_APPLICATION_SUBMITTED_QUEUE, true);
    }

    @Bean
    Queue adminDocumentStatusQueue() {
        return new Queue(ADMIN_DOCUMENT_STATUS_QUEUE, true);
    }

    @Bean
    Queue adminDecisionQueue() {
        return new Queue(ADMIN_DECISION_QUEUE, true);
    }

    @Bean
    Binding adminApplicationSubmittedBinding(Queue adminApplicationSubmittedQueue, DirectExchange finflowEventsExchange) {
        return BindingBuilder.bind(adminApplicationSubmittedQueue)
                .to(finflowEventsExchange)
                .with(APPLICATION_SUBMITTED_ROUTING_KEY);
    }

    @Bean
    Binding adminDocumentStatusBinding(Queue adminDocumentStatusQueue, DirectExchange finflowEventsExchange) {
        return BindingBuilder.bind(adminDocumentStatusQueue)
                .to(finflowEventsExchange)
                .with(DOCUMENT_STATUS_ROUTING_KEY);
    }

    @Bean
    Binding adminDecisionBinding(Queue adminDecisionQueue, DirectExchange finflowEventsExchange) {
        return BindingBuilder.bind(adminDecisionQueue)
                .to(finflowEventsExchange)
                .with(DECISION_ROUTING_KEY);
    }

    @Bean
    MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
