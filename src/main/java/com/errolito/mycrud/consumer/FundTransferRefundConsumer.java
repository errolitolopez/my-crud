package com.errolito.mycrud.consumer;

import com.errolito.mycrud.entity.Account;
import com.errolito.mycrud.enums.EventType;
import com.errolito.mycrud.event.FundTransferEvent;
import com.errolito.mycrud.producer.FundTransferProducer;
import com.errolito.mycrud.service.AccountService;
import com.errolito.mycrud.service.FundTransferAuditService;
import com.errolito.mycrud.shared.BaseFundTransferConsumer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

import static com.errolito.mycrud.enums.AuditStatus.*;
import static com.errolito.mycrud.enums.AuditType.REFUND;
import static com.errolito.mycrud.enums.EventType.AUDITED;
import static com.errolito.mycrud.enums.EventType.REFUNDED;

@Component
public class FundTransferRefundConsumer extends BaseFundTransferConsumer<FundTransferEvent> {
    private final AccountService accountService;
    private final FundTransferProducer producer;

    protected FundTransferRefundConsumer(FundTransferAuditService auditService,
                                         AccountService accountService,
                                         FundTransferProducer producer) {
        super(auditService);
        this.accountService = accountService;
        this.producer = producer;
    }

    @Override
    protected EventType eventType() {
        return REFUNDED;
    }

    @Override
    protected void handleEvent(FundTransferEvent event) {
        String originAccountNumber = event.getOriginAccountNumber();
        Optional<Account> accountOptional = accountService.findByAccountNumber(originAccountNumber);

        event.setAuditType(REFUND);
        event.setAuditStatus(SUCCESS);

        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();

            BigDecimal amount = event.getAmount();
            accountService.updateAccountBalance(originAccountNumber, account.getAccountBalance().add(amount));
        } else {
            int retryCount = event.getRetryCount();

            if (retryCount > 0) {
                event.setRetryCount(retryCount - 1);
                event.setAuditStatus(FAILED);
                producer.send(REFUNDED.topic(), event);
                return;
            } else {
                log.warn("Origin account number not found {}", originAccountNumber);

                event.setAuditStatus(ORIGIN_ACCOUNT_NUMBER_NOT_FOUND);
            }
        }

        producer.send(AUDITED.topic(), event);
    }
}