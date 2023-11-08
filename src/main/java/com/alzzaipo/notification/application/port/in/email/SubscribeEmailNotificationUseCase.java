package com.alzzaipo.notification.application.port.in.email;

import com.alzzaipo.common.Email;
import com.alzzaipo.common.Uid;

public interface SubscribeEmailNotificationUseCase {

    void subscribeEmailNotification(Uid memberUID, Email email);
}
