package com.errolito.mycrud.controller;

import com.errolito.mycrud.dto.ChatMessageRequest;
import com.errolito.mycrud.producer.ChatProducer;
import com.errolito.mycrud.shared.BaseController;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController extends BaseController {
    private final ChatProducer producer;

    public ChatController(
            ChatProducer producer
    ) {
        this.producer = producer;
    }

    @MessageMapping("/chat.send")
    public void send(ChatMessageRequest request) {
        producer.send(request);
    }
}