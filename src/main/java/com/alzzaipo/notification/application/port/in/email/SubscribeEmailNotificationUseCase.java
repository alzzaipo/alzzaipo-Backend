package com.alzzaipo.notification.application.port.in.email;

import com.alzzaipo.notification.application.port.dto.SubscribeEmailNotificationCommand;

public interface SubscribeEmailNotificationUseCase {

    void subscribe(SubscribeEmailNotificationCommand command);
}
