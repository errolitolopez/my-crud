package com.errolito.mycrud.dto;

import com.errolito.mycrud.enums.AccountType;
import com.errolito.mycrud.shared.ValidEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class AccountOpenRequest extends CustomerRequest {
    @ValidEnum(
            value = AccountType.class,
            message = "Invalid account type. Must be one of the allowed constants. (CHECKING, SAVINGS)"
    )
    @Schema(description = "Account Type (CHECKING, SAVINGS)", defaultValue = "SAVINGS")
    private String accountType;
}