package com.fincore.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class NotificationEvent {
    private String transactionId;
    private Long senderAccountId;
    private String senderEmail;
    private String receiverKey;
    private BigDecimal amount;
    private String description;
    private String status;
    private String failureReason;
}
