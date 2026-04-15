package com.errolito.mycrud.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class FundTransferRequest {
    @Schema(defaultValue = "10")
    @NotNull(message = "Amount is required")
    @Digits(integer = 10, fraction = 4, message = "Invalid amount")
    @DecimalMin(value = "0.0001", message = "Invalid amount")
    private BigDecimal amount;

    @Schema(defaultValue = "100054724248")
    @NotBlank(message = "Origin account number is required")
    private String originAccountNumber;

    @Schema(defaultValue = "100080952227")
    @NotBlank(message = "Destination account number is required")
    private String destinationAccountNumber;
}
