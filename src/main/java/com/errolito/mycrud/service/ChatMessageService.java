package com.errolito.mycrud.service;

import com.errolito.mycrud.dto.ChatMessageQuery;
import com.errolito.mycrud.entity.ChatMessage;
import com.errolito.mycrud.shared.BaseCrudService;

public interface ChatMessageService extends BaseCrudService<Long, ChatMessageQuery, ChatMessage> {
}
