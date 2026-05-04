package com.errolito.mycrud.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class AiChatHistoryRequest {
    private Integer id;
    private String conversationId;
    private String content;
    private String type;
    private LocalDateTime timestamp;
}
