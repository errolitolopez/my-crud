package com.errolito.mycrud.event;

import com.errolito.mycrud.enums.AuditStatus;
import com.errolito.mycrud.enums.AuditType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class FundTransferEvent {
    private String referenceId;
    private BigDecimal amount;
    private String originAccountNumber;
    private String destinationAccountNumber;

    private AuditType auditType;
    private AuditStatus auditStatus;
    private Integer retryCount = 3;
}
