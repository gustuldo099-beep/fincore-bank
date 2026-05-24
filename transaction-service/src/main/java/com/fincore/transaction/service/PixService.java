package com.fincore.transaction.service;

import com.fincore.transaction.dto.PixEvent;
import com.fincore.transaction.dto.PixRequest;
import com.fincore.transaction.dto.PixResponse;
import com.fincore.transaction.exception.BusinessException;
import com.fincore.transaction.kafka.PixEventProducer;
import com.fincore.transaction.model.PixStatus;
import com.fincore.transaction.model.PixTransaction;
import com.fincore.transaction.repository.PixTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PixService {

    private final PixTransactionRepository pixTransactionRepository;
    private final PixEventProducer pixEventProducer;

    @Transactional
    public PixResponse initiatePix(PixRequest request) {
        String senderEmail = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        // Cria registro da transação
        PixTransaction txn = PixTransaction.builder()
                .transactionId(UUID.randomUUID().toString())
                .senderAccountId(request.getSenderAccountId())
                .senderEmail(senderEmail)
                .receiverKey(request.getReceiverKey())
                .amount(request.getAmount())
                .description(request.getDescription())
                .status(PixStatus.PENDING)
                .build();

        pixTransactionRepository.save(txn);
        log.info("PIX iniciado: {} de {} para {}", txn.getTransactionId(), senderEmail, request.getReceiverKey());

        // Publica no Kafka para processamento assíncrono
        PixEvent event = PixEvent.builder()
                .transactionId(txn.getTransactionId())
                .senderAccountId(txn.getSenderAccountId())
                .senderEmail(txn.getSenderEmail())
                .receiverKey(txn.getReceiverKey())
                .amount(txn.getAmount())
                .description(txn.getDescription())
                .status(PixStatus.PENDING.name())
                .build();

        pixEventProducer.publishPixInitiated(event);

        return PixResponse.from(txn);
    }

    public PixResponse getTransaction(String transactionId) {
        return PixResponse.from(pixTransactionRepository
                .findByTransactionId(transactionId)
                .orElseThrow(() -> new BusinessException("Transação não encontrada: " + transactionId)));
    }

    public Page<PixResponse> getHistory(Long accountId, Pageable pageable) {
        return pixTransactionRepository
                .findBySenderAccountIdOrderByCreatedAtDesc(accountId, pageable)
                .map(PixResponse::from);
    }
}
