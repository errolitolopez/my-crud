package com.errolito.mycrud.repository;

import com.errolito.mycrud.entity.FundTransferAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FundTransferAuditRepository extends JpaRepository<FundTransferAudit, Integer>, JpaSpecificationExecutor<FundTransferAudit> {
    @Query("""
            SELECT fta FROM FundTransferAudit fta
            WHERE fta.originAccountNumber = :accountNumber OR fta.destinationAccountNumber = :accountNumber
            ORDER BY fta.createdDate DESC
            """)
    List<FundTransferAudit> findAllByAccountNumber(@Param("accountNumber") String accountNumber);
}