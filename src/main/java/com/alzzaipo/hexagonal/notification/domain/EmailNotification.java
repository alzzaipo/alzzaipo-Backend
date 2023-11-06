package com.alzzaipo.hexagonal.notification.domain;

import com.alzzaipo.hexagonal.common.Email;
import com.alzzaipo.hexagonal.common.Uid;
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
