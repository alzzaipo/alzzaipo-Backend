package com.alzzaipo.hexagonal.notification.application.port.out;

import com.alzzaipo.hexagonal.notification.application.port.dto.UpdateNotificationCriterionCommand;

public interface UpdateNotificationCriterionPort {

    void updateNotificationCriterion(UpdateNotificationCriterionCommand command);
}
