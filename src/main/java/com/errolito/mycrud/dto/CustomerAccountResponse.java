package com.errolito.mycrud.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class CustomerAccountResponse  {
    private Long id;
    private BigDecimal accountBalance;
    private String accountNumber;
    private String accountType;
}