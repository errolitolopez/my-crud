package com.errolito.mycrud.mapper;

import com.errolito.mycrud.dto.AiChatHistoryRequest;
import com.errolito.mycrud.dto.AiChatHistoryResponse;
import com.errolito.mycrud.entity.AiChatHistory;
import com.errolito.mycrud.shared.BaseMapper;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface AiChatHistoryMapper extends BaseMapper<AiChatHistoryRequest, AiChatHistory, AiChatHistoryResponse> {
}