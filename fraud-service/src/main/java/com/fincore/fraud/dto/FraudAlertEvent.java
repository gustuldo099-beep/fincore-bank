package com.fincore.fraud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class FraudAlertEvent {
    private String transactionId;
    private Long accountId;
    private String userEmail;
    private BigDecimal amount;
    private String ruleTriggered;
    private String severity;
    private String details;
}
