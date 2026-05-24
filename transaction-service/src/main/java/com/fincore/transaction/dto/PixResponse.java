package com.fincore.transaction.dto;

import com.fincore.transaction.model.PixTransaction;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder
public class PixResponse {
    private Long id;
    private String transactionId;
    private Long senderAccountId;
    private String receiverKey;
    private BigDecimal amount;
    private String description;
    private String status;
    private String failureReason;
    private LocalDateTime createdAt;

    public static PixResponse from(PixTransaction t) {
        return PixResponse.builder()
                .id(t.getId())
                .transactionId(t.getTransactionId())
                .senderAccountId(t.getSenderAccountId())
                .receiverKey(t.getReceiverKey())
                .amount(t.getAmount())
                .description(t.getDescription())
                .status(t.getStatus().name())
                .failureReason(t.getFailureReason())
                .createdAt(t.getCreatedAt())
                .build();
    }
}
