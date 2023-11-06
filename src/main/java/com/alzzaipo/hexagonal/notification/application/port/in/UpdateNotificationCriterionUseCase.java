package com.alzzaipo.hexagonal.notification.application.port.in;

import com.alzzaipo.hexagonal.notification.application.port.dto.UpdateNotificationCriterionCommand;

public interface UpdateNotificationCriterionUseCase {

    void updateNotificationCriterion(UpdateNotificationCriterionCommand command);
}
