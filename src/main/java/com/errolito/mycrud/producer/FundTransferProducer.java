package com.errolito.mycrud.producer;

import com.errolito.mycrud.event.FundTransferEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static org.springframework.util.StringUtils.hasText;

@Component
public class FundTransferProducer {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final KafkaTemplate<String, FundTransferEvent> kafka;

    public FundTransferProducer(KafkaTemplate<String, FundTransferEvent> kafka) {
        this.kafka = kafka;
    }

    public void send(String topic, FundTransferEvent event) {
        boolean eventPresent = event != null;

        String referenceId = eventPresent ? event.getReferenceId() : null;

        if (hasText(topic) && hasText(referenceId)) {
            try {
                kafka.send(topic, event);

                log.info("Event sent for topic={} with referenceId={}", topic, referenceId);
            } catch (Exception e) {
                log.error("Event sent failed for topic={} with referenceId={} encountered an error. {}", topic, referenceId, e.getMessage());
            }
        } else {
            log.warn("Event sent failed as topic, or referenceId is not present. topic={}, referenceId={}", topic, referenceId);
        }
    }
}