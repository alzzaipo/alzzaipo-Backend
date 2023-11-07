package com.alzzaipo.hexagonal.notification.application.port.in.email;

import com.alzzaipo.hexagonal.common.Email;
import com.alzzaipo.hexagonal.common.Uid;

public interface UpdateEmailNotificationUseCase {

    void updateEmailNotification(Uid memberUID, Email email);
}
