package com.fincore.account.dto;

import com.fincore.account.model.Account;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder
public class AccountResponse {
    private Long id;
    private String accountNumber;
    private String agency;
    private String ownerEmail;
    private String ownerName;
    private BigDecimal balance;
    private String accountType;
    private String status;
    private LocalDateTime createdAt;

    public static AccountResponse from(Account a) {
        return AccountResponse.builder()
                .id(a.getId())
                .accountNumber(a.getAccountNumber())
                .agency(a.getAgency())
                .ownerEmail(a.getOwnerEmail())
                .ownerName(a.getOwnerName())
                .balance(a.getBalance())
                .accountType(a.getAccountType().name())
                .status(a.getStatus().name())
                .createdAt(a.getCreatedAt())
                .build();
    }
}
