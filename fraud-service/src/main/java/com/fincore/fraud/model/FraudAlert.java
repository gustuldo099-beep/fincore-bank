package com.fincore.fraud.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fraud_alerts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FraudAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String transactionId;

    @Column(nullable = false)
    private Long accountId;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String ruleTriggered;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FraudSeverity severity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FraudStatus status;

    private String details;
    private LocalDateTime resolvedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) status = FraudStatus.OPEN;
        if (severity == null) severity = FraudSeverity.MEDIUM;
    }
}
