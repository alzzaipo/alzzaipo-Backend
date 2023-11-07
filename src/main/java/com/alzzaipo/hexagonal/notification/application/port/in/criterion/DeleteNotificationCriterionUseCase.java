package com.alzzaipo.hexagonal.notification.application.port.in.criterion;

import com.alzzaipo.hexagonal.notification.application.port.dto.DeleteNotificationCriterionCommand;

public interface DeleteNotificationCriterionUseCase {

    void deleteNotificationCriterion(DeleteNotificationCriterionCommand command);
}
