package com.alzzaipo.notification.application.port.dto;

import com.alzzaipo.common.Email;
import com.alzzaipo.common.LoginType;
import com.alzzaipo.common.Uid;
import lombok.Getter;

@Getter
public class SubscribeEmailNotificationCommand {

    private final Uid memberUID;
    private final LoginType loginType;
    private final Email email;

    public SubscribeEmailNotificationCommand(Uid memberUID, LoginType loginType, Email email) {
        this.memberUID = memberUID;
        this.loginType = loginType;
        this.email = email;
    }
}
