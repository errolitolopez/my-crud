package com.errolito.mycrud.security.dto;

import io.github.uncaughterrol.commons.utils.TokenGenerator;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RateLimitConsumeRequest {
    private String method;
    private String instance;
    private String ipAddress;

    public String getKey() {
        return TokenGenerator.compositeKey(ipAddress.replace(":", "."), method, instance);
    }
}