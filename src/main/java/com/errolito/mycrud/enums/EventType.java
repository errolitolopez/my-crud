package com.errolito.mycrud.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EventType {
    AUDITED("audited", "fund-transfer", true),
    COMPLETED("completed", "fund-transfer", true),
    CREDITED("credited", "fund-transfer", true),
    DEBITED("debited", "fund-transfer", true),
    FAILED("failed", "fund-transfer", true),
    REFUNDED("refunded", "fund-transfer", true),

    ;
    private final String topic;
    private final String group;
    private final Boolean autoStartup;

    public String topic() {
        return topic;
    }

    public String group() {
        return group;
    }

    public Boolean autoStartup() {
        return autoStartup;
    }
}