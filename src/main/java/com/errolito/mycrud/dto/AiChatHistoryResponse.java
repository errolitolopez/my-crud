package com.errolito.mycrud.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class AiChatHistoryResponse {
    private Integer id;
    private String conversationId;
    private String content;
    private String type;
    private LocalDateTime timestamp;
}