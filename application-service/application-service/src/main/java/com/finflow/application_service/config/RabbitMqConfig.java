package com.finflow.application_service.config;

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
    public static final String APPLICATION_DOCUMENT_STATUS_QUEUE = "finflow.application.document-status.queue";
    public static final String APPLICATION_DECISION_QUEUE = "finflow.application.decision.queue";
    public static final String APPLICATION_SUBMITTED_ROUTING_KEY = "loan.application.submitted";
    public static final String DOCUMENT_STATUS_ROUTING_KEY = "loan.document.status.changed";
    public static final String DECISION_ROUTING_KEY = "loan.decision.made";

    @Bean
    DirectExchange finflowEventsExchange() {
        return new DirectExchange(EVENTS_EXCHANGE, true, false);
    }

    @Bean
    Queue applicationDocumentStatusQueue() {
        return new Queue(APPLICATION_DOCUMENT_STATUS_QUEUE, true);
    }

    @Bean
    Queue applicationDecisionQueue() {
        return new Queue(APPLICATION_DECISION_QUEUE, true);
    }

    @Bean
    Binding applicationDocumentStatusBinding(Queue applicationDocumentStatusQueue, DirectExchange finflowEventsExchange) {
        return BindingBuilder.bind(applicationDocumentStatusQueue)
                .to(finflowEventsExchange)
                .with(DOCUMENT_STATUS_ROUTING_KEY);
    }

    @Bean
    Binding applicationDecisionBinding(Queue applicationDecisionQueue, DirectExchange finflowEventsExchange) {
        return BindingBuilder.bind(applicationDecisionQueue)
                .to(finflowEventsExchange)
                .with(DECISION_ROUTING_KEY);
    }

    @Bean
    MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
