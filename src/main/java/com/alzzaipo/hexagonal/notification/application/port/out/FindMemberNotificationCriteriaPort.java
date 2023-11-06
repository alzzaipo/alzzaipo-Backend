package com.alzzaipo.hexagonal.notification.application.port.out;

import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.notification.domain.NotificationCriterion;

import java.util.List;

public interface FindMemberNotificationCriteriaPort {

    List<NotificationCriterion> findMemberNotificationCriteria(Uid memberUID);
}
