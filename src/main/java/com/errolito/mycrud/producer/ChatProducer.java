package com.errolito.mycrud.producer;

import com.errolito.mycrud.config.RabbitConfig;
import com.errolito.mycrud.dto.ChatMessageRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatProducer {
    private final RabbitTemplate template;

    @Autowired
    public ChatProducer(RabbitTemplate template) {
        this.template = template;
    }

    public void send(ChatMessageRequest request) {
        template.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_KEY,
                request
        );
    }
}