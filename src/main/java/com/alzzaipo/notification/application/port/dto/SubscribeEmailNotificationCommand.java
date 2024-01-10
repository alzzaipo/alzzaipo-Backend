package com.alzzaipo.notification.application.port.dto;

import com.alzzaipo.common.Id;
import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.email.domain.EmailVerificationCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubscribeEmailNotificationCommand {

    private final Id memberId;
    private final Email email;
    private final EmailVerificationCode emailVerificationCode;
}
