package com.errolito.mycrud.facade.impl;

import com.errolito.mycrud.dto.ChatMessageQuery;
import com.errolito.mycrud.dto.ChatMessageRequest;
import com.errolito.mycrud.dto.ChatMessageResponse;
import com.errolito.mycrud.entity.ChatMessage;
import com.errolito.mycrud.facade.ChatMessageFacade;
import com.errolito.mycrud.mapper.ChatMessageMapper;
import com.errolito.mycrud.service.ChatMessageService;
import com.errolito.mycrud.shared.BaseCrudFacadeImpl;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageFacadeImpl
        extends BaseCrudFacadeImpl<Long, ChatMessageQuery, ChatMessageRequest, ChatMessage, ChatMessageResponse>
        implements ChatMessageFacade {

    protected ChatMessageFacadeImpl(ChatMessageMapper mapper, ChatMessageService service) {
        super(mapper, service);
    }
}