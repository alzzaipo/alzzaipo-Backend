package com.alzzaipo.hexagonal.notification.application.port.out.criterion;

import com.alzzaipo.hexagonal.common.Uid;

public interface DeleteNotificationCriterionPort {

    void deleteNotificationCriterion(Uid notificationCriterionPort);
}
