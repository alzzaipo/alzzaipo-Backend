package com.alzzaipo.notification.application.port.out.criterion;

import com.alzzaipo.notification.domain.criterion.NotificationCriterion;

import java.util.List;

public interface FindAllNotificationCriterionPort {

    List<NotificationCriterion> findAllNotificationCriterion();
}
