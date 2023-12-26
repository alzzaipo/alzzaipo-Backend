package com.alzzaipo.notification.application.port.out.criterion;

import com.alzzaipo.common.Id;
import com.alzzaipo.notification.domain.criterion.NotificationCriterion;

import java.util.List;

public interface FindMemberNotificationCriteriaPort {

    List<NotificationCriterion> findMemberNotificationCriteria(Id memberId);
}
