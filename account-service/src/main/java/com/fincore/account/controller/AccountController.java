package com.fincore.account.controller;

import com.fincore.account.dto.*;
import com.fincore.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> create(@Valid @RequestBody CreateAccountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccount(id));
    }

    @GetMapping("/number/{number}")
    public ResponseEntity<AccountResponse> getByNumber(@PathVariable String number) {
        return ResponseEntity.ok(accountService.getAccountByNumber(number));
    }

    @GetMapping("/my")
    public ResponseEntity<List<AccountResponse>> myAccounts(Authentication auth) {
        return ResponseEntity.ok(accountService.getAccountsByEmail(auth.getName()));
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<AccountResponse> deposit(@PathVariable Long id,
                                                    @Valid @RequestBody DepositRequest request) {
        return ResponseEntity.ok(accountService.deposit(id, request));
    }

    @GetMapping("/{id}/statement")
    public ResponseEntity<Page<TransactionResponse>> statement(
            @PathVariable Long id,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(accountService.getStatement(id, pageable));
    }
}
