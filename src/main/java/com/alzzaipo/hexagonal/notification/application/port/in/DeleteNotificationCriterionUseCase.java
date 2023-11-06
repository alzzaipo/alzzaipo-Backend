package com.alzzaipo.hexagonal.notification.application.port.in;

import com.alzzaipo.hexagonal.notification.application.port.dto.DeleteNotificationCriterionCommand;

public interface DeleteNotificationCriterionUseCase {

    void deleteNotificationCriterion(DeleteNotificationCriterionCommand command);
}
