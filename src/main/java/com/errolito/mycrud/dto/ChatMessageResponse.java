package com.errolito.mycrud.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@ToString
public class ChatMessageResponse {
    private String roomId;
    private String sender;
    private String content;
    private Instant timestamp;
}