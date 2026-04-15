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

import static com.errolito.mycrud.enums.AuditStatus.DESTINATION_ACCOUNT_NUMBER_NOT_FOUND;
import static com.errolito.mycrud.enums.AuditStatus.SUCCESS;
import static com.errolito.mycrud.enums.AuditType.CREDIT;
import static com.errolito.mycrud.enums.EventType.*;

@Component
public class FundTransferCreditConsumer extends BaseFundTransferConsumer<FundTransferEvent> {
    private final AccountService accountService;
    private final FundTransferProducer producer;

    protected FundTransferCreditConsumer(
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
        return CREDITED;
    }

    @Override
    protected void handleEvent(FundTransferEvent event) {
        String destinationAccountNumber = event.getDestinationAccountNumber();
        Optional<Account> accountOptional = accountService.findByAccountNumber(destinationAccountNumber);

        event.setAuditType(CREDIT);
        event.setAuditStatus(SUCCESS);

        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();

            BigDecimal amount = event.getAmount();
            accountService.updateAccountBalance(destinationAccountNumber, account.getAccountBalance().add(amount));
        } else {
            log.warn("Destination account number not found {}", destinationAccountNumber);

            event.setAuditStatus(DESTINATION_ACCOUNT_NUMBER_NOT_FOUND);
        }

        if (!SUCCESS.equals(event.getAuditStatus())) {
            producer.send(REFUNDED.topic(), event);
        }

        producer.send(AUDITED.topic(), event);
    }
}