package com.alzzaipo.notification.application.port.out.criterion;

import com.alzzaipo.notification.application.port.dto.UpdateNotificationCriterionCommand;

public interface UpdateNotificationCriterionPort {

    void updateNotificationCriterion(UpdateNotificationCriterionCommand command);
}
