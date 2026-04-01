package com.finflow.document_service.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String EVENTS_EXCHANGE = "finflow.events.exchange";
    public static final String DOCUMENT_STATUS_ROUTING_KEY = "loan.document.status.changed";

    @Bean
    DirectExchange finflowEventsExchange() {
        return new DirectExchange(EVENTS_EXCHANGE, true, false);
    }

    @Bean
    MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
