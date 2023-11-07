package com.alzzaipo.hexagonal.notification.application.port.in.email;

import com.alzzaipo.hexagonal.common.Email;
import com.alzzaipo.hexagonal.common.Uid;

public interface SubscribeEmailNotificationUseCase {

    void subscribeEmailNotification(Uid memberUID, Email email);
}
