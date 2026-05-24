package com.fincore.transaction.kafka;

import com.fincore.transaction.dto.PixEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PixEventProducer {

    private final KafkaTemplate<String, PixEvent> kafkaTemplate;

    @Value("${kafka.topics.pix-initiated}")
    private String pixInitiatedTopic;

    @Value("${kafka.topics.pix-completed}")
    private String pixCompletedTopic;

    @Value("${kafka.topics.pix-failed}")
    private String pixFailedTopic;

    public void publishPixInitiated(PixEvent event) {
        log.info("Publicando PIX iniciado: {}", event.getTransactionId());
        kafkaTemplate.send(pixInitiatedTopic, event.getTransactionId(), event);
    }

    public void publishPixCompleted(PixEvent event) {
        log.info("Publicando PIX concluído: {}", event.getTransactionId());
        kafkaTemplate.send(pixCompletedTopic, event.getTransactionId(), event);
    }

    public void publishPixFailed(PixEvent event) {
        log.warn("Publicando PIX falhou: {} - {}", event.getTransactionId(), event.getFailureReason());
        kafkaTemplate.send(pixFailedTopic, event.getTransactionId(), event);
    }
}
