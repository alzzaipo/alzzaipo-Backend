package com.alzzaipo.notification.application.port.in.email;

import com.alzzaipo.common.Uid;

public interface UnsubscribeEmailNotificationUseCase {

    void unsubscribeEmailNotification(Uid memberUID);
}
