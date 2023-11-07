package com.alzzaipo.hexagonal.notification.application.port.in.email;

import com.alzzaipo.hexagonal.common.Uid;

public interface UnsubscribeEmailNotificationUseCase {

    void unsubscribeEmailNotification(Uid memberUID);
}
