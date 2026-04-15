package com.errolito.mycrud.facade.impl;

import com.errolito.mycrud.dto.FundTransferAuditQuery;
import com.errolito.mycrud.dto.FundTransferAuditRequest;
import com.errolito.mycrud.dto.FundTransferAuditResponse;
import com.errolito.mycrud.entity.FundTransferAudit;
import com.errolito.mycrud.facade.FundTransferAuditFacade;
import com.errolito.mycrud.mapper.FundTransferAuditMapper;
import com.errolito.mycrud.service.FundTransferAuditService;
import com.errolito.mycrud.shared.BaseCrudFacadeImpl;
import org.springframework.stereotype.Component;

@Component
public class FundTransferAuditFacadeImpl
        extends BaseCrudFacadeImpl<Integer, FundTransferAuditQuery, FundTransferAuditRequest, FundTransferAudit, FundTransferAuditResponse>
        implements FundTransferAuditFacade {

    protected FundTransferAuditFacadeImpl(FundTransferAuditMapper mapper, FundTransferAuditService service) {
        super(mapper, service);
    }
}