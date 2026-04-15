package com.errolito.mycrud.service;

import com.errolito.mycrud.dto.AccountQuery;
import com.errolito.mycrud.entity.Account;
import com.errolito.mycrud.shared.BaseCrudService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountService extends BaseCrudService<Integer, AccountQuery, Account> {
    Optional<Account> findByAccountNumber(String originAccountNumber);

    void updateAccountBalance(String originAccountNumber, BigDecimal accountBalance);

    List<Account> findAllByAccountNumberIn(List<String> accountNumbers);
}