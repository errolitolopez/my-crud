package com.errolito.mycrud.facade;

import com.errolito.mycrud.dto.ChatMessageQuery;
import com.errolito.mycrud.dto.ChatMessageRequest;
import com.errolito.mycrud.dto.ChatMessageResponse;
import com.errolito.mycrud.shared.BaseCrudFacade;

public interface ChatMessageFacade
        extends BaseCrudFacade<Long, ChatMessageQuery, ChatMessageRequest, ChatMessageResponse> {
}