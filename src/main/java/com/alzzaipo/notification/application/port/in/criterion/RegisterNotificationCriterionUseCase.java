package com.alzzaipo.notification.application.port.in.criterion;

import com.alzzaipo.notification.application.port.dto.RegisterNotificationCriterionCommand;

public interface RegisterNotificationCriterionUseCase {

    void registerNotificationCriterion(RegisterNotificationCriterionCommand command);
}
