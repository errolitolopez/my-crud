package com.errolito.mycrud.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AiChatRequest {
    private String message;
    private String conversationId;
    private String model;
}