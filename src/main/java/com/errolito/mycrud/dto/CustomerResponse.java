package com.errolito.mycrud.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class CustomerResponse {
    private Long id;
    private String fullName;

    private List<CustomerAccountResponse> accounts = new ArrayList<>();

    public List<CustomerAccountResponse> getAccounts() {
        if (accounts != null && !accounts.isEmpty()) {
            accounts = accounts.stream()
                    .sorted(Comparator.comparing(CustomerAccountResponse::getId))
                    .toList();
        }
        return accounts;
    }
}