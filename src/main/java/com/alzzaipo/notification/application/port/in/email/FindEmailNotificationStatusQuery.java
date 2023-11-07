package com.alzzaipo.notification.application.port.in.email;

import com.alzzaipo.common.Uid;
import com.alzzaipo.notification.application.port.dto.EmailNotificationStatus;

public interface FindEmailNotificationStatusQuery {

    EmailNotificationStatus findEmailNotificationStatus(Uid memberUID);
}
