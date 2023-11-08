package com.alzzaipo.notification.application.port.in.criterion;

import com.alzzaipo.notification.application.port.dto.UpdateNotificationCriterionCommand;

public interface UpdateNotificationCriterionUseCase {

    void updateNotificationCriterion(UpdateNotificationCriterionCommand command);
}
