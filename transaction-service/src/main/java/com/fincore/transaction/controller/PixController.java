package com.fincore.transaction.controller;

import com.fincore.transaction.dto.PixRequest;
import com.fincore.transaction.dto.PixResponse;
import com.fincore.transaction.service.PixService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class PixController {

    private final PixService pixService;

    @PostMapping("/pix")
    public ResponseEntity<PixResponse> initiatePix(@Valid @RequestBody PixRequest request) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(pixService.initiatePix(request));
    }

    @GetMapping("/pix/{transactionId}")
    public ResponseEntity<PixResponse> getTransaction(@PathVariable String transactionId) {
        return ResponseEntity.ok(pixService.getTransaction(transactionId));
    }

    @GetMapping("/pix/history/{accountId}")
    public ResponseEntity<Page<PixResponse>> getHistory(
            @PathVariable Long accountId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(pixService.getHistory(accountId, pageable));
    }
}
