package com.errolito.mycrud.security;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthRefreshTokenRequest {
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
}