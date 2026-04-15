package com.errolito.mycrud.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundTransferResponse {
    private String referenceId;
    private String message;
    private String status;
    private String statusMessage;
}