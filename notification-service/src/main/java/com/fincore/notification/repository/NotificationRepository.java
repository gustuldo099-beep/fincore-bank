package com.fincore.notification.repository;

import com.fincore.notification.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByUserEmailOrderByCreatedAtDesc(String email, Pageable pageable);
    List<Notification> findByUserEmailAndReadFalse(String email);
    long countByUserEmailAndReadFalse(String email);
}
