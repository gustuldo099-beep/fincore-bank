package com.fincore.account.dto;

import com.fincore.account.model.AccountType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateAccountRequest {
    @NotBlank @Email
    private String ownerEmail;
    @NotBlank
    private String ownerName;
    private AccountType accountType;
}
