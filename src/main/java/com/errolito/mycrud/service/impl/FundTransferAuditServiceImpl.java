package com.errolito.mycrud.service.impl;

import com.errolito.mycrud.dto.FundTransferAuditQuery;
import com.errolito.mycrud.entity.FundTransferAudit;
import com.errolito.mycrud.enums.AuditType;
import com.errolito.mycrud.repository.FundTransferAuditRepository;
import com.errolito.mycrud.service.FundTransferAuditService;
import com.errolito.mycrud.shared.BaseCrudServiceImpl;
import com.errolito.mycrud.shared.SpecBuilder;
import io.github.uncaughterrol.commons.exception.ExceptionFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;

@Service
public class FundTransferAuditServiceImpl
        extends BaseCrudServiceImpl<Integer, FundTransferAuditQuery, FundTransferAudit, FundTransferAuditRepository>
        implements FundTransferAuditService {

    protected FundTransferAuditServiceImpl(FundTransferAuditRepository repository) {
        super(repository);
    }

    @Override
    protected Specification<FundTransferAudit> buildLikeSpec(FundTransferAuditQuery fundTransferAuditQuery) {
        return (root, criteriaQuery, builder) ->
                SpecBuilder.of(root, builder)
                        .andLike("referenceId", fundTransferAuditQuery.getReferenceId())
                        .andLike("auditType", fundTransferAuditQuery.getAuditType())
                        .andLike("auditStatus", fundTransferAuditQuery.getAuditStatus())
                        .andLike("originAccountNumber", fundTransferAuditQuery.getOriginAccountNumber())
                        .andLike("destinationAccountNumber", fundTransferAuditQuery.getDestinationAccountNumber())
                        .build();
    }

    @Override
    protected Specification<FundTransferAudit> buildEqualSpec(FundTransferAuditQuery fundTransferAuditQuery) {
        return (root, criteriaQuery, builder) ->
                SpecBuilder.of(root, builder)
                        .andEqual("referenceId", fundTransferAuditQuery.getReferenceId())
                        .andEqual("auditType", fundTransferAuditQuery.getAuditType())
                        .andLike("auditStatus", fundTransferAuditQuery.getAuditStatus())
                        .andEqual("originAccountNumber", fundTransferAuditQuery.getOriginAccountNumber())
                        .andEqual("destinationAccountNumber", fundTransferAuditQuery.getDestinationAccountNumber())
                        .build();
    }

    @Override
    protected Supplier<RuntimeException> notFoundException() {
        return () -> ExceptionFactory.notFound("Fund transfer audit not found");
    }

    @Override
    public boolean alreadyProcessed(AuditType auditType, String referenceId) {
        FundTransferAuditQuery query = FundTransferAuditQuery
                .builder()
                .referenceId(referenceId)
                .auditType(auditType.name())
                .build();

        return existsByQuery(query);
    }

    @Override
    @Transactional
    public List<FundTransferAudit> findAllByAccountNumber(String accountNumber) {
        return repository.findAllByAccountNumber(accountNumber);
    }
}
