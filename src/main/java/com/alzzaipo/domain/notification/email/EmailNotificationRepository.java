package com.alzzaipo.domain.notification.email;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmailNotificationRepository extends JpaRepository<EmailNotification, Long> {
    @Query("SELECT e FROM EmailNotification e WHERE e.email = ?1")
    Optional<EmailNotification> findByEmail(String email);

}
