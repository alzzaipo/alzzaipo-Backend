package com.alzzaipo.notification.application.port.in.criterion;

import com.alzzaipo.notification.application.port.dto.DeleteNotificationCriterionCommand;

public interface DeleteNotificationCriterionUseCase {

    void deleteNotificationCriterion(DeleteNotificationCriterionCommand command);
}
