package com.fincore.fraud.dto;

import com.fincore.fraud.model.FraudAlert;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder
public class FraudAlertResponse {
    private Long id;
    private String transactionId;
    private Long accountId;
    private String userEmail;
    private BigDecimal amount;
    private String ruleTriggered;
    private String severity;
    private String status;
    private String details;
    private LocalDateTime createdAt;

    public static FraudAlertResponse from(FraudAlert a) {
        return FraudAlertResponse.builder()
                .id(a.getId())
                .transactionId(a.getTransactionId())
                .accountId(a.getAccountId())
                .userEmail(a.getUserEmail())
                .amount(a.getAmount())
                .ruleTriggered(a.getRuleTriggered())
                .severity(a.getSeverity().name())
                .status(a.getStatus().name())
                .details(a.getDetails())
                .createdAt(a.getCreatedAt())
                .build();
    }
}
