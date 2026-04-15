package com.errolito.mycrud.controller;

import com.errolito.mycrud.dto.ChatMessageQuery;
import com.errolito.mycrud.dto.ChatMessageRequest;
import com.errolito.mycrud.dto.ChatMessageResponse;
import com.errolito.mycrud.facade.ChatMessageFacade;
import com.errolito.mycrud.shared.BaseCrudController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/chat-messages")
public class ChatMessageController
        extends BaseCrudController<Long, ChatMessageQuery, ChatMessageRequest, ChatMessageResponse> {

    protected ChatMessageController(ChatMessageFacade facade) {
        super(facade);
    }
}