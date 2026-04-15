package com.errolito.mycrud.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class FundTransferAuditRequest {
    private Long id;
    private BigDecimal amount;
    private Instant createdDate;
    private String auditStatus;
    private String auditType;
    private String destinationAccountNumber;
    private String originAccountNumber;
    private String referenceId;
}