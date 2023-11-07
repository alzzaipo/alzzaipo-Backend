package com.alzzaipo.hexagonal.notification.application.port.out.criterion;

import com.alzzaipo.hexagonal.notification.application.port.dto.UpdateNotificationCriterionCommand;

public interface UpdateNotificationCriterionPort {

    void updateNotificationCriterion(UpdateNotificationCriterionCommand command);
}
