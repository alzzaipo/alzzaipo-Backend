package com.alzzaipo.notification.application.port.out.criterion;

import com.alzzaipo.common.Uid;
import com.alzzaipo.notification.domain.criterion.NotificationCriterion;

import java.util.Optional;

public interface FindNotificationCriterionPort {

    Optional<NotificationCriterion> findNotificationCriterion(Uid notifcationCriterionUID);
}
