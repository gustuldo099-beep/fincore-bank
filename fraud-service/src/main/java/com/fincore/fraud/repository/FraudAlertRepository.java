package com.fincore.fraud.repository;

import com.fincore.fraud.model.FraudAlert;
import com.fincore.fraud.model.FraudStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FraudAlertRepository extends JpaRepository<FraudAlert, Long> {
    Page<FraudAlert> findByStatusOrderByCreatedAtDesc(FraudStatus status, Pageable pageable);
    List<FraudAlert> findByUserEmailOrderByCreatedAtDesc(String email);
    long countByAccountIdAndCreatedAtAfter(Long accountId, LocalDateTime after);
    List<FraudAlert> findByTransactionId(String transactionId);
    long countByUserEmailAndCreatedAtAfter(String email, LocalDateTime after);
}
