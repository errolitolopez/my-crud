package com.errolito.mycrud.mapper;

import com.errolito.mycrud.dto.ChatMessageRequest;
import com.errolito.mycrud.dto.ChatMessageResponse;
import com.errolito.mycrud.entity.ChatMessage;
import com.errolito.mycrud.shared.BaseMapper;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface ChatMessageMapper extends BaseMapper<ChatMessageRequest, ChatMessage, ChatMessageResponse> {
}