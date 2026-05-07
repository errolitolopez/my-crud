package com.errolito.mycrud.repository;

import com.errolito.mycrud.entity.Account;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer>, JpaSpecificationExecutor<Account> {
    @EntityGraph(attributePaths = "customer")
    Optional<Account> findByAccountNumber(String originAccountNumber);

    @Modifying
    @Query("""
            UPDATE Account a
            SET a.accountBalance = :accountBalance
            WHERE a.accountNumber = :accountNumber
            """)
    void updateAccountBalance(@Param("accountNumber") String accountNumber, @Param("accountBalance") BigDecimal accountBalance);

    @EntityGraph(attributePaths = {"customer"})
    List<Account> findAllByAccountNumberIn(List<String> accountNumbers);

    @EntityGraph(attributePaths = {"customer"})
    List<Account> findAllByAccountNumberNotInOrderByCustomerFullNameAsc(List<String> accountNumbers);
}