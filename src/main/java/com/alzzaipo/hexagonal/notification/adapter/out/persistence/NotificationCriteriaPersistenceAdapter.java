package com.alzzaipo.hexagonal.notification.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationCriteriaPersistenceAdapter {

    private final NewNotificationCriteriaRepository notificationCriteriaRepository;
}
