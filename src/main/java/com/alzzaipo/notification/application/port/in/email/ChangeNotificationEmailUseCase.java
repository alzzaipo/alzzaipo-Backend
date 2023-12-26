package com.alzzaipo.notification.application.port.in.email;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.Id;

public interface ChangeNotificationEmailUseCase {

    void changeNotificationEmail(Id memberId, Email email);
}
