package com.alzzaipo.notification.domain.email;

import com.alzzaipo.common.Email;
import com.alzzaipo.common.Uid;
import lombok.Getter;

@Getter
public class EmailNotification {

    private final Uid memberUID;
    private Email email;

    public EmailNotification(Uid memberUID, Email email) {
        this.memberUID = memberUID;
        this.email = email;
    }
}
