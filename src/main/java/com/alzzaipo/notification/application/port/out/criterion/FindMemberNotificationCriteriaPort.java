package com.alzzaipo.notification.application.port.out.criterion;

import com.alzzaipo.common.Uid;
import com.alzzaipo.notification.domain.criterion.NotificationCriterion;

import java.util.List;

public interface FindMemberNotificationCriteriaPort {

    List<NotificationCriterion> findMemberNotificationCriteria(Uid memberUID);
}
