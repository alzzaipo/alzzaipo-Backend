package com.alzzaipo.hexagonal.notification.adapter.out.persistence.email;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NewEmailNotificationRepository extends JpaRepository<EmailNotificationJpaEntity, Long> {
}
