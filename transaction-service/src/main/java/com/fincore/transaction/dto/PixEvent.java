package com.fincore.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PixEvent {
    private String transactionId;
    private Long senderAccountId;
    private String senderEmail;
    private String receiverKey;
    private Long receiverAccountId;
    private BigDecimal amount;
    private String description;
    private String status;
    private String failureReason;
}
