package com.alzzaipo.hexagonal.notification.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NewNotificationCriteriaRepository extends JpaRepository<NotificationCriteriaJpaEntity, Long> {
}
