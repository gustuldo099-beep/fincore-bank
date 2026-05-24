package com.fincore.account.repository;

import com.fincore.account.model.Account;
import com.fincore.account.model.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByOwnerEmail(String ownerEmail);
    boolean existsByAccountNumber(String accountNumber);
    List<Account> findByOwnerEmailAndStatus(String ownerEmail, AccountStatus status);
}
