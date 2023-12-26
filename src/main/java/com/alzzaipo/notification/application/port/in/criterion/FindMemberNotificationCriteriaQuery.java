package com.alzzaipo.notification.application.port.in.criterion;

import com.alzzaipo.common.Id;
import com.alzzaipo.notification.application.port.dto.NotificationCriterionView;

import java.util.List;

public interface FindMemberNotificationCriteriaQuery {

    List<NotificationCriterionView> findMemberNotificationCriteria(Id memberId);
}
