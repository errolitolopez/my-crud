package com.errolito.mycrud.facade.impl;

import com.errolito.mycrud.dto.*;
import com.errolito.mycrud.entity.Account;
import com.errolito.mycrud.entity.Customer;
import com.errolito.mycrud.enums.AccountType;
import com.errolito.mycrud.facade.CustomerFacade;
import com.errolito.mycrud.mapper.CustomerMapper;
import com.errolito.mycrud.service.AccountService;
import com.errolito.mycrud.service.CustomerService;
import com.errolito.mycrud.shared.BaseCrudFacadeImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;

import static io.github.uncaughterrol.commons.utils.TokenGenerator.secureBankAccountNumber;

@Component
public class CustomerFacadeImpl
        extends BaseCrudFacadeImpl<Integer, CustomerQuery, CustomerRequest, Customer, CustomerResponse>
        implements CustomerFacade {

    private final AccountService accountService;

    protected CustomerFacadeImpl(
            AccountService accountService,
            CustomerMapper mapper,
            CustomerService service
    ) {
        super(mapper, service);
        this.accountService = accountService;
    }

    @Override
    @Transactional
    public CustomerResponse openAccount(AccountOpenRequest request) {
        Customer customer = new Customer();
        customer.setFullName(request.getFullName());

        Account account = buildAccount(request.getAccountType());
        account.setCustomer(customer);

        customer.setAccounts(Collections.singleton(account));

        Customer saved = service.save(customer);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CustomerResponse addAccount(AccountAddRequest request) {
        Customer customer = service.getById(request.getCustomerId());

        Account account = buildAccount(request.getAccountType());
        account.setCustomer(customer);

        customer.getAccounts().add(account);

        Customer saved = service.save(customer);
        return mapper.toResponse(saved);
    }

    private Account buildAccount(String accountType) {
        Account account = new Account();
        account.setAccountBalance(BigDecimal.valueOf(1000));
        account.setAccountType(AccountType.valueOf(accountType));
        account.setAccountNumber(generateAccountNumber());
        return account;
    }

    private String generateAccountNumber() {
        String accountNumber = secureBankAccountNumber(1000, 12);
        if (accountService.existsByQuery(AccountQuery.builder().accountNumber(accountNumber).build())) {
            return generateAccountNumber();
        }
        return accountNumber;
    }
}