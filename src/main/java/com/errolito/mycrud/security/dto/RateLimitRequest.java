package com.errolito.mycrud.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RateLimitRequest {
    @NotBlank(message = "URL is required")
    private String url;

    @NotNull(message = "Capacity is required")
    @Positive(message = "Capacity must not be zero or negative")
    private Integer capacity;

    @NotNull(message = "Refill is required")
    @Positive(message = "Refill must not be zero or negative")
    private Integer refill;
}