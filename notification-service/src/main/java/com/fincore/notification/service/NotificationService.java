package com.fincore.notification.service;

import com.fincore.notification.dto.NotificationResponse;
import com.fincore.notification.model.Notification;
import com.fincore.notification.model.NotificationType;
import com.fincore.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public void createNotification(String email, NotificationType type,
                                    String title, String message, String referenceId) {
        Notification notification = Notification.builder()
                .userEmail(email)
                .type(type)
                .title(title)
                .message(message)
                .referenceId(referenceId)
                .read(false)
                .build();

        notificationRepository.save(notification);
        log.info("Notificação criada para {}: {}", email, title);

        // Aqui no futuro: enviar email via SMTP, push notification, etc.
        sendNotification(email, title, message);
    }

    public Page<NotificationResponse> getNotifications(String email, Pageable pageable) {
        return notificationRepository
                .findByUserEmailOrderByCreatedAtDesc(email, pageable)
                .map(NotificationResponse::from);
    }

    public long countUnread(String email) {
        return notificationRepository.countByUserEmailAndReadFalse(email);
    }

    @Transactional
    public void markAsRead(Long id) {
        notificationRepository.findById(id).ifPresent(n -> {
            n.setRead(true);
            notificationRepository.save(n);
        });
    }

    private void sendNotification(String email, String title, String message) {
        // Simulação de envio — no futuro integrar com:
        // - JavaMailSender (email)
        // - Firebase (push)
        // - Twilio (SMS)
        log.info("📧 [SIMULADO] Enviando para {}: {} - {}", email, title, message);
    }
}
