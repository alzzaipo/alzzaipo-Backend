package com.alzzaipo.notification.application.port.out.criterion;

public interface CheckNotificationCriterionOwnershipPort {

	boolean checkOwnership(long memberId, long notificationCriterionId);

}
