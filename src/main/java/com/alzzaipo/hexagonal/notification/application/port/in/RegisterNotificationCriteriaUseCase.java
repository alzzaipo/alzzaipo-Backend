package com.alzzaipo.hexagonal.notification.application.port.in;

import com.alzzaipo.hexagonal.notification.application.port.dto.RegisterNotificationCriteriaCommand;

public interface RegisterNotificationCriteriaUseCase {

    void registerNotificationCriteria(RegisterNotificationCriteriaCommand command);
}
