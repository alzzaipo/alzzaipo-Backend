package com.alzzaipo.hexagonal.notification.application.port.in.email;

import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.notification.application.port.dto.EmailNotificationStatus;

public interface FindEmailNotificationStatusQuery {

    EmailNotificationStatus findEmailNotificationStatus(Uid memberUID);
}
