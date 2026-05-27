package com.fincore.fraud.controller;

import com.fincore.fraud.dto.FraudAlertResponse;
import com.fincore.fraud.model.FraudStatus;
import com.fincore.fraud.service.FraudDetectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/fraud")
@RequiredArgsConstructor
public class FraudController {

    private final FraudDetectionService fraudDetectionService;

    @GetMapping("/alerts")
    public ResponseEntity<Page<FraudAlertResponse>> getAlerts(
            @RequestParam(defaultValue = "OPEN") FraudStatus status,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(fraudDetectionService.getAlerts(status, pageable));
    }

    @GetMapping("/alerts/user/{email}")
    public ResponseEntity<List<FraudAlertResponse>> getAlertsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(fraudDetectionService.getAlertsByEmail(email));
    }

    @PutMapping("/alerts/{id}/resolve")
    public ResponseEntity<FraudAlertResponse> resolveAlert(
            @PathVariable Long id,
            @RequestParam FraudStatus status) {
        return ResponseEntity.ok(fraudDetectionService.resolveAlert(id, status));
    }
}
