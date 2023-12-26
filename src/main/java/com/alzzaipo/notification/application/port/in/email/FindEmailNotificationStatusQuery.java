package com.alzzaipo.notification.application.port.in.email;

import com.alzzaipo.common.Id;
import com.alzzaipo.notification.application.port.dto.EmailNotificationStatus;

public interface FindEmailNotificationStatusQuery {

    EmailNotificationStatus findStatus(Id memberId);
}
