package com.alzzaipo.notification.application.port.out.criterion;

import com.alzzaipo.common.Id;

public interface CheckNotificationCriterionOwnershipPort {

	boolean checkOwnership(Id memberId, Id notificationCriterionId);

}
