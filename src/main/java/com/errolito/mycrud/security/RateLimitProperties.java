package com.errolito.mycrud.security;

import com.errolito.mycrud.config.PropertySourceConfig;
import com.errolito.mycrud.security.dto.RateLimit;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNullElse;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "rate-limit")
@PropertySource(value = "classpath:rate-limit.yml", factory = PropertySourceConfig.class)
public class RateLimitProperties {
    private Integer capacity;
    private Integer refill;
    private List<RateLimit> rateLimits = new ArrayList<>();

    public List<RateLimit> getRateLimits() {
        if (rateLimits == null || rateLimits.isEmpty()) {
            return Collections.emptyList();
        }

        return rateLimits
                .stream()
                .map(r ->
                        RateLimit.builder()
                                .url(r.getUrl())
                                .capacity(requireNonNullElse(r.getCapacity(), capacity))
                                .refill(requireNonNullElse(r.getRefill(), refill))
                                .build()
                ).toList();
    }
}