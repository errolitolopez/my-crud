package com.errolito.mycrud.repository;

import com.errolito.mycrud.entity.FundTransferAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FundTransferAuditRepository extends JpaRepository<FundTransferAudit, Integer>, JpaSpecificationExecutor<FundTransferAudit> {
}