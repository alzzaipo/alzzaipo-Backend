package com.alzzaipo.hexagonal.notification.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NewNotificationCriterionRepository extends JpaRepository<NotificationCriterionJpaEntity, Long> {
}
