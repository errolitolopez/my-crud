package com.errolito.mycrud.service;

import com.errolito.mycrud.dto.FundTransferAuditQuery;
import com.errolito.mycrud.entity.FundTransferAudit;
import com.errolito.mycrud.enums.AuditType;
import com.errolito.mycrud.shared.BaseCrudService;

import java.util.List;

public interface FundTransferAuditService extends BaseCrudService<Integer, FundTransferAuditQuery, FundTransferAudit> {
    boolean alreadyProcessed(AuditType auditType, String referenceId);

    List<FundTransferAudit>  findAllByAccountNumber(String accountNumber);
}
