package com.alzzaipo.notification.application.port.dto;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.Id;
import lombok.Getter;

@Getter
public class SubscribeEmailNotificationCommand {

    private final Id memberId;
    private final Email email;

    public SubscribeEmailNotificationCommand(Id memberId, Email email) {
        this.memberId = memberId;
        this.email = email;
    }
}
