package com.alzzaipo.hexagonal.notification.application.port.out.criterion;

import com.alzzaipo.hexagonal.notification.domain.criterion.NotificationCriterion;

import java.util.List;

public interface FindAllNotificationCriterionPort {

    List<NotificationCriterion> findAllNotificationCriterion();
}
