package com.alzzaipo.hexagonal.notification.application.port.out;

import com.alzzaipo.hexagonal.common.Uid;

public interface UnsubscribeEmailNotificationPort {

    void unsubscribeEmailNotification(Uid memberUID);
}
