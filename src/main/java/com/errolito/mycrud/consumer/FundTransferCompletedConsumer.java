package com.errolito.mycrud.consumer;

import com.errolito.mycrud.builder.FundTransferNotificationBuilder;
import com.errolito.mycrud.dto.FundTransferResponse;
import com.errolito.mycrud.entity.Account;
import com.errolito.mycrud.entity.Notification;
import com.errolito.mycrud.enums.AuditStatus;
import com.errolito.mycrud.enums.AuditType;
import com.errolito.mycrud.enums.EventType;
import com.errolito.mycrud.event.FundTransferEvent;
import com.errolito.mycrud.service.AccountService;
import com.errolito.mycrud.service.FundTransferAuditService;
import com.errolito.mycrud.service.NotificationService;
import com.errolito.mycrud.shared.BaseFundTransferConsumer;
import io.github.uncaughterrol.commons.utils.SmartStringUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.errolito.mycrud.enums.AuditStatus.SUCCESS;
import static com.errolito.mycrud.enums.AuditType.CREDIT;
import static com.errolito.mycrud.enums.AuditType.DEBIT;
import static com.errolito.mycrud.enums.EventType.COMPLETED;

@Component
public class FundTransferCompletedConsumer extends BaseFundTransferConsumer<FundTransferEvent> {
    private final AccountService accountService;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    protected FundTransferCompletedConsumer(
            AccountService accountService,
            FundTransferAuditService auditService,
            NotificationService notificationService,
            SimpMessagingTemplate messagingTemplate
    ) {
        super(auditService);

        this.accountService = accountService;
        this.notificationService = notificationService;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    protected EventType eventType() {
        return COMPLETED;
    }

    @Override
    public boolean alreadyProcessed(AuditType auditType, String referenceId) {
        return false;
    }

    @Override
    @Transactional
    protected void handleEvent(FundTransferEvent event) {
        String originAccountNumber = event.getOriginAccountNumber();
        String destinationAccountNumber = event.getDestinationAccountNumber();

        List<String> accountNumbers = List.of(originAccountNumber, destinationAccountNumber);

        Map<String, Account> accountMap = accountService.findAllByAccountNumberIn(accountNumbers)
                .stream()
                .collect(Collectors.toMap(Account::getAccountNumber, Function.identity()));

        Account originAccount = accountMap.get(originAccountNumber);
        Account destinationAccount = accountMap.get(destinationAccountNumber);

        FundTransferNotificationBuilder notificationBuilder = FundTransferNotificationBuilder
                .builder()
                .event(event)
                .originCustomer(originAccount != null ? originAccount.getCustomer() : null)
                .destinationCustomer(destinationAccount != null ? destinationAccount.getCustomer() : null)
                .build();

        List<Notification> notifications = notificationBuilder
                .notifications();

        notificationService.saveAll(notifications);

        AuditStatus auditStatus = event.getAuditStatus();

        FundTransferResponse response = new FundTransferResponse();
        response.setReferenceId(event.getReferenceId());
        response.setMessage(notificationBuilder.originMessage());
        response.setStatus(auditStatus.name());
        response.setStatusMessage(SmartStringUtils.toSentenceCase(auditStatus.name()));

        String destination = "/topic/transfer-updates/" + event.getReferenceId();
        if (Set.of(DEBIT, CREDIT).contains(event.getAuditType()) && !SUCCESS.equals(auditStatus)) {
            messagingTemplate.convertAndSend(destination, response);
            return;
        }

        if (CREDIT.equals(event.getAuditType()) && SUCCESS.equals(auditStatus)) {
            messagingTemplate.convertAndSend(destination, response);
        }
    }
}