package com.errolito.mycrud.builder;

import com.errolito.mycrud.entity.Customer;
import com.errolito.mycrud.entity.Notification;
import com.errolito.mycrud.event.FundTransferEvent;
import lombok.Builder;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.errolito.mycrud.enums.AuditType.CREDIT;
import static org.springframework.util.StringUtils.hasText;

@Setter
@Builder
public class FundTransferNotificationBuilder {
    private FundTransferEvent event;
    private Customer originCustomer;
    private Customer destinationCustomer;

    public Notification origin() {
        if (originCustomer == null) {
            return null;
        }

        return Notification
                .builder()
                .customer(originCustomer)
                .message(originMessage())
                .build();
    }

    public Notification destination() {
        if (destinationCustomer == null) {
            return null;
        }

        return Notification
                .builder()
                .customer(destinationCustomer)
                .message(destinationMessage())
                .build();
    }

    public List<Notification> notifications() {
        return Stream.of(origin(), destination())
                .filter(Objects::nonNull)
                .filter(notification -> hasText(notification.getMessage()))
                .toList();
    }

    public String originMessage() {
        if (event == null || event.getAuditType() == null) {
            return null;
        }

        switch (event.getAuditType()) {
            case CREDIT -> {
                switch (event.getAuditStatus()) {
                    case SUCCESS -> {
                        return String.format(
                                "You have successfully sent %s to account %s. Ref: %s",
                                event.getAmount(),
                                event.getOriginAccountNumber(),
                                event.getReferenceId()
                        );
                    }
                    case DESTINATION_ACCOUNT_NUMBER_NOT_FOUND -> {
                        return String.format(
                                "Invalid destination account %s. Transfer of %s was not processed. Ref: %s",
                                event.getDestinationAccountNumber(),
                                event.getAmount(),
                                event.getReferenceId()
                        );
                    }
                    case null, default -> {
                    }
                }
            }
            case REFUND -> {
                return String.format(
                        "Refunded %s back to account %s successfully. Ref: %s",
                        event.getAmount(),
                        event.getOriginAccountNumber(),
                        event.getReferenceId()
                );
            }
            case DEBIT -> {
                switch (event.getAuditStatus()) {
                    case INSUFFICIENT_FUNDS -> {
                        return String.format(
                                "Transaction failed due to insufficient funds. Unable to send %s to account %s. Ref %s",
                                event.getAmount(),
                                event.getDestinationAccountNumber(),
                                event.getReferenceId()
                        );
                    }
                    case ORIGIN_ACCOUNT_NUMBER_NOT_FOUND -> {
                        return String.format(
                                "Invalid origin account %s. Transfer of %s was not processed. Ref: %s",
                                event.getOriginAccountNumber(),
                                event.getAmount(),
                                event.getReferenceId()
                        );
                    }
                    case null, default -> {
                    }
                }
            }
        }

        return null;
    }

    public String destinationMessage() {
        if (event == null || event.getAuditType() == null) {
            return null;
        }

        if (CREDIT.equals(event.getAuditType())) {
            return String.format(
                    "You have received %s from account %s. Ref: %s",
                    event.getAmount(),
                    event.getOriginAccountNumber(),
                    event.getReferenceId()
            );
        }

        return null;
    }
}