package com.alzzaipo.notification.application.port.dto;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.Uid;
import lombok.Getter;

@Getter
public class SubscribeEmailNotificationCommand {

    private final Uid memberUID;
    private final Email email;

    public SubscribeEmailNotificationCommand(Uid memberUID, Email email) {
        this.memberUID = memberUID;
        this.email = email;
    }
}
