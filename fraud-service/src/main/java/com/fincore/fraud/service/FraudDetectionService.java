package com.fincore.fraud.service;

import com.fincore.fraud.dto.FraudAlertEvent;
import com.fincore.fraud.dto.FraudAlertResponse;
import com.fincore.fraud.dto.FraudAnalysisEvent;
import com.fincore.fraud.model.FraudAlert;
import com.fincore.fraud.model.FraudSeverity;
import com.fincore.fraud.model.FraudStatus;
import com.fincore.fraud.repository.FraudAlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FraudDetectionService {

    private final FraudAlertRepository fraudAlertRepository;
    private final KafkaTemplate<String, FraudAlertEvent> kafkaTemplate;

    @Value("${fraud.max-amount:5000.00}")
    private BigDecimal maxAmount;

    @Value("${fraud.max-daily-transactions:10}")
    private int maxDailyTransactions;

    @Value("${fraud.suspicious-amount:1000.00}")
    private BigDecimal suspiciousAmount;

    @Value("${kafka.topics.fraud-alert}")
    private String fraudAlertTopic;

    @Transactional
    public void analyze(FraudAnalysisEvent event) {
        log.info("Analisando transação: {} | R$ {} | conta: {}",
                event.getTransactionId(), event.getAmount(), event.getSenderAccountId());

        List<String> triggeredRules = new ArrayList<>();

        // Regra 1: Valor acima do limite máximo
        if (event.getAmount().compareTo(maxAmount) > 0) {
            triggeredRules.add("AMOUNT_EXCEEDED_LIMIT");
            createAlert(event, "AMOUNT_EXCEEDED_LIMIT", FraudSeverity.HIGH,
                    String.format("Valor R$ %.2f excede o limite máximo de R$ %.2f",
                            event.getAmount(), maxAmount));
        }

        // Regra 2: Valor suspeito (entre 1000 e 5000)
        if (event.getAmount().compareTo(suspiciousAmount) >= 0 &&
            event.getAmount().compareTo(maxAmount) < 0) {
            triggeredRules.add("SUSPICIOUS_AMOUNT");
            createAlert(event, "SUSPICIOUS_AMOUNT", FraudSeverity.MEDIUM,
                    String.format("Valor R$ %.2f considerado suspeito para monitoramento",
                            event.getAmount()));
        }

        // Regra 3: Muitas transações no dia
        long todayCount = fraudAlertRepository.countByAccountIdAndCreatedAtAfter(
                event.getSenderAccountId(), LocalDateTime.now().withHour(0).withMinute(0));

        if (todayCount >= maxDailyTransactions) {
            triggeredRules.add("HIGH_FREQUENCY");
            createAlert(event, "HIGH_FREQUENCY", FraudSeverity.CRITICAL,
                    String.format("Conta %d realizou %d transações hoje (limite: %d)",
                            event.getSenderAccountId(), todayCount, maxDailyTransactions));
        }

        // Regra 4: Chave PIX inválida (começa com números suspeitos)
        if (event.getReceiverKey() != null && event.getReceiverKey().startsWith("00000")) {
            triggeredRules.add("SUSPICIOUS_RECEIVER_KEY");
            createAlert(event, "SUSPICIOUS_RECEIVER_KEY", FraudSeverity.HIGH,
                    "Chave PIX do destinatário com padrão suspeito: " + event.getReceiverKey());
        }

        if (triggeredRules.isEmpty()) {
            log.info("Transação {} aprovada — nenhuma regra de fraude disparada", event.getTransactionId());
        } else {
            log.warn("Transação {} flagrada por {} regras: {}", event.getTransactionId(),
                    triggeredRules.size(), triggeredRules);
        }
    }

    private void createAlert(FraudAnalysisEvent event, String rule,
                              FraudSeverity severity, String details) {
        FraudAlert alert = FraudAlert.builder()
                .transactionId(event.getTransactionId())
                .accountId(event.getSenderAccountId())
                .userEmail(event.getSenderEmail())
                .amount(event.getAmount())
                .ruleTriggered(rule)
                .severity(severity)
                .status(FraudStatus.OPEN)
                .details(details)
                .build();

        fraudAlertRepository.save(alert);
        log.warn("🚨 ALERTA DE FRAUDE [{} - {}]: {} | Transação: {}",
                severity, rule, details, event.getTransactionId());

        // Publica alerta no Kafka
        FraudAlertEvent alertEvent = FraudAlertEvent.builder()
                .transactionId(event.getTransactionId())
                .accountId(event.getSenderAccountId())
                .userEmail(event.getSenderEmail())
                .amount(event.getAmount())
                .ruleTriggered(rule)
                .severity(severity.name())
                .details(details)
                .build();

        kafkaTemplate.send(fraudAlertTopic, event.getTransactionId(), alertEvent);
    }

    public Page<FraudAlertResponse> getAlerts(FraudStatus status, Pageable pageable) {
        return fraudAlertRepository.findByStatusOrderByCreatedAtDesc(status, pageable)
                .map(FraudAlertResponse::from);
    }

    public List<FraudAlertResponse> getAlertsByEmail(String email) {
        return fraudAlertRepository.findByUserEmailOrderByCreatedAtDesc(email)
                .stream().map(FraudAlertResponse::from).toList();
    }

    @Transactional
    public FraudAlertResponse resolveAlert(Long id, FraudStatus newStatus) {
        FraudAlert alert = fraudAlertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alerta não encontrado: " + id));
        alert.setStatus(newStatus);
        if (newStatus == FraudStatus.RESOLVED || newStatus == FraudStatus.FALSE_POSITIVE) {
            alert.setResolvedAt(LocalDateTime.now());
        }
        return FraudAlertResponse.from(fraudAlertRepository.save(alert));
    }
}
