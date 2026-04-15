package com.errolito.mycrud.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class CustomerRequest {
    @Pattern(regexp = "^[a-zA-Z'.-]+(?: [a-zA-Z'.-]+)+$", message = "Invalid full name")
    @NotBlank(message = "Full name is required")
    @Schema(description = "Customer's full name", defaultValue = "John Doe")
    private String fullName;
}