package com.errolito.mycrud.consumer;

import com.errolito.mycrud.entity.FundTransferAudit;
import com.errolito.mycrud.enums.EventType;
import com.errolito.mycrud.event.FundTransferEvent;
import com.errolito.mycrud.mapper.FundTransferAuditMapper;
import com.errolito.mycrud.producer.FundTransferProducer;
import com.errolito.mycrud.service.FundTransferAuditService;
import com.errolito.mycrud.shared.BaseFundTransferConsumer;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.errolito.mycrud.enums.AuditType.*;
import static com.errolito.mycrud.enums.EventType.AUDITED;
import static com.errolito.mycrud.enums.EventType.COMPLETED;

@Component
public class FundTransferAuditConsumer extends BaseFundTransferConsumer<FundTransferEvent> {
    private final FundTransferAuditMapper fundTransferAuditMapper;
    private final FundTransferAuditService auditService;
    private final FundTransferProducer producer;

    protected FundTransferAuditConsumer(FundTransferAuditService auditService,
                                        FundTransferAuditMapper fundTransferAuditMapper,
                                        FundTransferProducer producer) {
        super(auditService);

        this.fundTransferAuditMapper = fundTransferAuditMapper;
        this.auditService = auditService;
        this.producer = producer;
    }

    @Override
    protected EventType eventType() {
        return AUDITED;
    }

    @Override
    protected void handleEvent(FundTransferEvent event) {
        FundTransferAudit audit = fundTransferAuditMapper.toEntity(event);
        auditService.save(audit);

        if (Set.of(CREDIT, REFUND, DEBIT).contains(event.getAuditType())) {
            producer.send(COMPLETED.topic(), event);
        }
    }
}