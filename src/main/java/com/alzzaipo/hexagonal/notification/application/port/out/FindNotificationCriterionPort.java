package com.alzzaipo.hexagonal.notification.application.port.out;

import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.notification.domain.NotificationCriterion;

import java.util.Optional;

public interface FindNotificationCriterionPort {

    Optional<NotificationCriterion> findNotificationCriterion(Uid notifcationCriterionUID);
}
