package com.fincore.notification.repository;

import com.fincore.notification.model.PixTransaction;
import com.fincore.notification.model.PixStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface PixTransactionRepository extends JpaRepository<PixTransaction, Long> {
    Optional<PixTransaction> findByTransactionId(String transactionId);
    Page<PixTransaction> findBySenderAccountIdOrderByCreatedAtDesc(Long accountId, Pageable pageable);
    List<PixTransaction> findBySenderEmailOrderByCreatedAtDesc(String email);
    List<PixTransaction> findByStatus(PixStatus status);
}
