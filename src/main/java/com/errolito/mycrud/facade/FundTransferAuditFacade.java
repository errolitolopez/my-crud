package com.errolito.mycrud.facade;

import com.errolito.mycrud.dto.FundTransferAuditQuery;
import com.errolito.mycrud.dto.FundTransferAuditRequest;
import com.errolito.mycrud.dto.FundTransferAuditResponse;
import com.errolito.mycrud.shared.BaseCrudFacade;

public interface FundTransferAuditFacade
        extends BaseCrudFacade<Integer, FundTransferAuditQuery, FundTransferAuditRequest, FundTransferAuditResponse> {
}