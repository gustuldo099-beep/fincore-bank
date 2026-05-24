package com.fincore.notification.consumer;

import com.fincore.notification.dto.NotificationEvent;
import com.fincore.notification.model.NotificationType;
import com.fincore.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PixEventConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "${kafka.topics.pix-completed}", groupId = "notification-service")
    public void onPixCompleted(NotificationEvent event) {
        log.info("Notificação PIX concluído: {}", event.getTransactionId());

        String title = "PIX enviado com sucesso! ✅";
        String message = String.format(
            "Seu PIX de R$ %.2f para %s foi processado com sucesso. ID: %s",
            event.getAmount(), event.getReceiverKey(), event.getTransactionId()
        );

        notificationService.createNotification(
            event.getSenderEmail(),
            NotificationType.PIX_COMPLETED,
            title, message,
            event.getTransactionId()
        );
    }

    @KafkaListener(topics = "${kafka.topics.pix-failed}", groupId = "notification-service")
    public void onPixFailed(NotificationEvent event) {
        log.warn("Notificação PIX falhou: {}", event.getTransactionId());

        String title = "PIX falhou ❌";
        String message = String.format(
            "Seu PIX de R$ %.2f para %s falhou. Motivo: %s",
            event.getAmount(), event.getReceiverKey(),
            event.getFailureReason() != null ? event.getFailureReason() : "Erro interno"
        );

        notificationService.createNotification(
            event.getSenderEmail(),
            NotificationType.PIX_FAILED,
            title, message,
            event.getTransactionId()
        );
    }
}
