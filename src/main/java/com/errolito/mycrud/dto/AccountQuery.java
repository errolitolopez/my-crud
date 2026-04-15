package com.errolito.mycrud.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class AccountQuery {
    private Long id;
    private BigDecimal accountBalance;
    private Long customerId;
    private String accountNumber;
    private String accountType;
}