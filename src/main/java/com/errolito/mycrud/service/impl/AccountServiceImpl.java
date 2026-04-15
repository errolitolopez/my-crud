package com.errolito.mycrud.service.impl;

import com.errolito.mycrud.dto.AccountQuery;
import com.errolito.mycrud.entity.Account;
import com.errolito.mycrud.repository.AccountRepository;
import com.errolito.mycrud.service.AccountService;
import com.errolito.mycrud.shared.BaseCrudServiceImpl;
import com.errolito.mycrud.shared.SpecBuilder;
import io.github.uncaughterrol.commons.exception.ExceptionFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class AccountServiceImpl
        extends BaseCrudServiceImpl<Integer, AccountQuery, Account, AccountRepository>
        implements AccountService {

    protected AccountServiceImpl(AccountRepository repository) {
        super(repository);
    }

    @Override
    protected Specification<Account> buildLikeSpec(AccountQuery query) {
        return (root, criteriaQuery, builder) ->
                SpecBuilder.of(root, builder)
                        .build();
    }

    @Override
    protected Specification<Account> buildEqualSpec(AccountQuery query) {
        return (root, criteriaQuery, builder) ->
                SpecBuilder.of(root, builder)
                        .andEqual("accountNumber", query.getAccountNumber())
                        .build();
    }

    @Override
    protected Supplier<RuntimeException> notFoundException() {
        return () -> ExceptionFactory.notFound("Account not found");
    }

    @Override
    @Transactional
    public Optional<Account> findByAccountNumber(String originAccountNumber) {
        return repository.findByAccountNumber(originAccountNumber);
    }

    @Override
    @Transactional
    public void updateAccountBalance(String originAccountNumber, BigDecimal accountBalance) {
        repository.updateAccountBalance(originAccountNumber, accountBalance);
    }

    @Override
    @Transactional
    public List<Account> findAllByAccountNumberIn(List<String> accountNumbers) {
        return repository.findAllByAccountNumberIn(accountNumbers);
    }
}