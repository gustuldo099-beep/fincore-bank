package com.fincore.fraud.consumer;

import com.fincore.fraud.dto.FraudAnalysisEvent;
import com.fincore.fraud.service.FraudDetectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PixEventConsumer {

    private final FraudDetectionService fraudDetectionService;

    @KafkaListener(topics = "${kafka.topics.pix-initiated}", groupId = "fraud-service")
    public void onPixInitiated(FraudAnalysisEvent event) {
        log.info("Fraud check para transação: {}", event.getTransactionId());
        fraudDetectionService.analyze(event);
    }
}
