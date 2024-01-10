package com.alzzaipo.notification.application.port.in.email;

import com.alzzaipo.notification.application.port.dto.ChangeNotificationEmailCommand;

public interface ChangeNotificationEmailUseCase {

    void changeNotificationEmail(ChangeNotificationEmailCommand command);
}
