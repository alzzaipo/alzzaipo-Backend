package com.alzzaipo.hexagonal.notification.application.port.out.criterion;

import com.alzzaipo.hexagonal.notification.domain.criterion.NotificationCriterion;

public interface RegisterNotificationCriterionPort {

    void registerNotificationCriterion(NotificationCriterion notificationCriterion);
}
