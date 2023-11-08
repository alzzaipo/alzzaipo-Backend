package com.alzzaipo.notification.application.port.in.email;

import com.alzzaipo.common.Email;
import com.alzzaipo.common.Uid;

public interface UpdateEmailNotificationUseCase {

    void updateEmailNotification(Uid memberUID, Email email);
}
