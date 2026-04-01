package com.errolito.mycrud.health;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class InstancePortHealthIndicator implements HealthIndicator {

    @Value("${instance.port:unknown}")
    private String instancePort;

    @Override
    public Health health() {
        return Health.up()
                .withDetail("port", instancePort)
                .build();
    }
}