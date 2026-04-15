package com.errolito.mycrud.consumer;

import com.errolito.mycrud.entity.Account;
import com.errolito.mycrud.enums.AuditStatus;
import com.errolito.mycrud.enums.EventType;
import com.errolito.mycrud.event.FundTransferEvent;
import com.errolito.mycrud.producer.FundTransferProducer;
import com.errolito.mycrud.service.AccountService;
import com.errolito.mycrud.service.FundTransferAuditService;
import com.errolito.mycrud.shared.BaseFundTransferConsumer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

import static com.errolito.mycrud.enums.AuditStatus.SUCCESS;
import static com.errolito.mycrud.enums.AuditType.DEBIT;
import static com.errolito.mycrud.enums.EventType.*;

@Component
public class FundTransferDebitConsumer extends BaseFundTransferConsumer<FundTransferEvent> {
    private final AccountService accountService;
    private final FundTransferProducer producer;

    public FundTransferDebitConsumer(
            AccountService accountService,
            FundTransferAuditService auditService,
            FundTransferProducer producer
    ) {
        super(auditService);

        this.accountService = accountService;
        this.producer = producer;
    }

    @Override
    protected EventType eventType() {
        return DEBITED;
    }

    @Override
    protected void handleEvent(FundTransferEvent event) {
        String originAccountNumber = event.getOriginAccountNumber();
        Optional<Account> accountOptional = accountService.findByAccountNumber(originAccountNumber);

        event.setAuditType(DEBIT);
        event.setAuditStatus(SUCCESS);

        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();

            BigDecimal amount = event.getAmount();

            if (account.hasSufficientFunds(amount)) {
                accountService.updateAccountBalance(originAccountNumber, account.getAccountBalance().subtract(amount));
            } else {
                log.warn("Insufficient funds for account {}", originAccountNumber);
                event.setAuditStatus(AuditStatus.INSUFFICIENT_FUNDS);
            }
        } else {
            log.warn("Origin account number not found {}", originAccountNumber);
            event.setAuditStatus(AuditStatus.ORIGIN_ACCOUNT_NUMBER_NOT_FOUND);
        }

        if (SUCCESS.equals(event.getAuditStatus())) {
            producer.send(CREDITED.topic(), event);
        }

        producer.send(AUDITED.topic(), event);
    }
}