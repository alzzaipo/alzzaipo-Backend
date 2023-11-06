package com.alzzaipo.hexagonal.notification.application.port.out;

import com.alzzaipo.hexagonal.notification.domain.NotificationCriterion;

public interface RegisterNotificationCriterionPort {

    void registerNotificationCriterion(NotificationCriterion notificationCriterion);
}
