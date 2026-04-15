package com.errolito.mycrud.consumer;

import com.errolito.mycrud.config.RabbitConfig;
import com.errolito.mycrud.dto.ChatMessageRequest;
import com.errolito.mycrud.dto.ChatMessageResponse;
import com.errolito.mycrud.facade.ChatMessageFacade;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class ChatConsumer {
    private final SimpMessagingTemplate template;
    private final ChatMessageFacade facade;

    public ChatConsumer(SimpMessagingTemplate template, ChatMessageFacade facade) {
        this.template = template;
        this.facade = facade;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void receive(ChatMessageRequest request) {
        ChatMessageResponse response = facade.save(request);
        String roomId = response.getRoomId();
        template.convertAndSend("/topic/chat/" + roomId, response);
    }
}