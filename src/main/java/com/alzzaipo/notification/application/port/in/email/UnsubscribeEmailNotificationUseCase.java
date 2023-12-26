package com.alzzaipo.notification.application.port.in.email;

import com.alzzaipo.common.Id;

public interface UnsubscribeEmailNotificationUseCase {

    void unsubscribeEmailNotification(Id memberId);
}
