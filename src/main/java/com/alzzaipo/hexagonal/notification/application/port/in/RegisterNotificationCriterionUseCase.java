package com.alzzaipo.hexagonal.notification.application.port.in;

import com.alzzaipo.hexagonal.notification.application.port.dto.RegisterNotificationCriterionCommand;

public interface RegisterNotificationCriterionUseCase {

    void registerNotificationCriterion(RegisterNotificationCriterionCommand command);
}
