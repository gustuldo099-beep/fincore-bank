package com.fincore.transaction.kafka;

import com.fincore.transaction.dto.PixEvent;
import com.fincore.transaction.model.PixStatus;
import com.fincore.transaction.model.PixTransaction;
import com.fincore.transaction.repository.PixTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PixEventConsumer {

    private final PixTransactionRepository pixTransactionRepository;
    private final PixEventProducer pixEventProducer;

    @KafkaListener(topics = "${kafka.topics.pix-initiated}", groupId = "transaction-service")
    @Transactional
    public void processPixInitiated(PixEvent event) {
        log.info("Processando PIX: {}", event.getTransactionId());

        PixTransaction txn = pixTransactionRepository
                .findByTransactionId(event.getTransactionId())
                .orElse(null);

        if (txn == null) {
            log.error("Transação não encontrada: {}", event.getTransactionId());
            return;
        }

        try {
            // Simula validação de negócio
            validatePix(event);

            // Atualiza status para PROCESSING
            txn.setStatus(PixStatus.PROCESSING);
            pixTransactionRepository.save(txn);

            // Simula processamento (ex: debitar conta remetente)
            log.info("Debitando R$ {} da conta {}", event.getAmount(), event.getSenderAccountId());

            // Marca como COMPLETED
            txn.setStatus(PixStatus.COMPLETED);
            txn.setReceiverAccountId(event.getReceiverAccountId());
            pixTransactionRepository.save(txn);

            // Publica evento de conclusão
            event.setStatus(PixStatus.COMPLETED.name());
            pixEventProducer.publishPixCompleted(event);

            log.info("PIX concluído com sucesso: {}", event.getTransactionId());

        } catch (Exception e) {
            log.error("Erro ao processar PIX {}: {}", event.getTransactionId(), e.getMessage());

            txn.setStatus(PixStatus.FAILED);
            txn.setFailureReason(e.getMessage());
            pixTransactionRepository.save(txn);

            event.setStatus(PixStatus.FAILED.name());
            event.setFailureReason(e.getMessage());
            pixEventProducer.publishPixFailed(event);
        }
    }

    private void validatePix(PixEvent event) {
        if (event.getAmount() == null || event.getAmount().signum() <= 0) {
            throw new IllegalArgumentException("Valor inválido para PIX");
        }
        if (event.getReceiverKey() == null || event.getReceiverKey().isBlank()) {
            throw new IllegalArgumentException("Chave PIX inválida");
        }
    }
}
