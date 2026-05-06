package com.errolito.mycrud.security.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RateLimit {
    private String url;
    private Integer capacity;
    private Integer refill;
}