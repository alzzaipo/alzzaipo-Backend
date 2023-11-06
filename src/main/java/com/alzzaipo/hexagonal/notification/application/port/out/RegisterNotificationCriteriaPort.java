package com.alzzaipo.hexagonal.notification.application.port.out;

import com.alzzaipo.hexagonal.notification.domain.NotificationCriteria;

public interface RegisterNotificationCriteriaPort {

    void registerNotificationCriteria(NotificationCriteria notificationCriteria);
}
