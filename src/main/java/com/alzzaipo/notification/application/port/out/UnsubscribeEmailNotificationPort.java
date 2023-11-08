package com.alzzaipo.notification.application.port.out;

import com.alzzaipo.common.Uid;

public interface UnsubscribeEmailNotificationPort {

    void unsubscribeEmailNotification(Uid memberUID);
}
