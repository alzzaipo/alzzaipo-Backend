package com.alzzaipo.hexagonal.notification.application.port.in.criterion;

import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.notification.application.port.dto.NotificationCriterionView;

import java.util.List;

public interface FindMemberNotificationCriteriaQuery {

    List<NotificationCriterionView> findMemberNotificationCriteria(Uid memberUID);
}
