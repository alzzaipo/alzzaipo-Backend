package com.alzzaipo.notification.application.port.out.email;

import com.alzzaipo.common.Id;

public interface DeleteEmailNotificationPort {

    void delete(Id memberId);
}
