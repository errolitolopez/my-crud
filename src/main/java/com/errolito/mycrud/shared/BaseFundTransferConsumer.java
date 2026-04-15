package com.errolito.mycrud.shared;

import com.errolito.mycrud.enums.AuditType;
import com.errolito.mycrud.enums.EventType;
import com.errolito.mycrud.event.FundTransferEvent;
import com.errolito.mycrud.service.FundTransferAuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

import static org.springframework.util.StringUtils.hasText;

public abstract class BaseFundTransferConsumer<E extends FundTransferEvent> {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    protected final FundTransferAuditService auditService;

    protected BaseFundTransferConsumer(FundTransferAuditService auditService) {
        this.auditService = auditService;
    }

    protected abstract EventType eventType();

    protected abstract void handleEvent(E event);

    protected void handleException(E event, Exception e) {
        log.debug("An exception occurred while processing event. {}", e.getMessage());
    }

    public String topic() {
        return eventType().topic();
    }

    public String groupId() {
        return eventType().group();
    }

    public boolean autoStartup() {
        return eventType().autoStartup();
    }

    public boolean alreadyProcessed(AuditType auditType, String referenceId) {
        if (auditType == null) {
            return false;
        }

        return auditService.alreadyProcessed(auditType, referenceId);
    }

    @KafkaListener(
            topics = "#{__listener.topic()}",
            groupId = "#{__listener.groupId()}",
            autoStartup = "#{__listener.autoStartup()}"
    )
    public void listen(E event) {
        String topic = topic();
        boolean eventPresent = event != null;

        String referenceId = eventPresent ? event.getReferenceId() : null;

        if (hasText(referenceId)) {
            if (!alreadyProcessed(event.getAuditType(), referenceId)) {
                try {
                    log.info("Event processing for topic={} with referenceId={}", topic, referenceId);

                    handleEvent(event);
                } catch (Exception e) {

                    log.warn("Event processing failed for topic={} with referenceId={} encountered an error. {}", topic, referenceId, e.getMessage());

                    handleException(event, e);
                }
            } else {
                log.warn("Event processing skipped for topic={} with referenceId={} already processed.", topic, referenceId);
            }
        } else {
            log.warn("Event processing failed for topic={} as referenceId is not present.", topic);
        }
    }
}