package com.errolito.mycrud.dto;

import lombok.*;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class NotificationRequest {
    private Long id;
    private Instant createdDate;
    private Long customerId;
    private String message;
}