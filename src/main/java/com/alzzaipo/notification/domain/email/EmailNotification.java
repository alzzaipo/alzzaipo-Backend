package com.alzzaipo.notification.domain.email;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.Id;
import lombok.Getter;

@Getter
public class EmailNotification {

    private final Id memberId;
    private final Email email;

    public EmailNotification(Id memberId, Email email) {
        this.memberId = memberId;
        this.email = email;
    }
}
