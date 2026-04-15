package com.errolito.mycrud.dto;

import com.errolito.mycrud.enums.AccountType;
import com.errolito.mycrud.shared.ValidEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class AccountAddRequest {
    @NotNull(message = "Customer id is required")
    private Integer customerId;

    @ValidEnum(
            value = AccountType.class,
            message = "Invalid account type. Must be one of the allowed constants. (CHECKING, SAVINGS)"
    )
    @Schema(description = "Account Type (CHECKING, SAVINGS)", defaultValue = "SAVINGS")
    private String accountType;
}
