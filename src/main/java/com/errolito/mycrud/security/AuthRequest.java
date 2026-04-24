package com.errolito.mycrud.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthRequest {
    @NotBlank(message = "Username is required")
    @Schema(defaultValue = "admin")
    private String username;

    @NotBlank(message = "Password is required")
    @Schema(defaultValue = "Password123!")
    private String password;
}