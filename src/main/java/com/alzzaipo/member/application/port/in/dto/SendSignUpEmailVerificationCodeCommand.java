package com.alzzaipo.member.application.port.in.dto;

import com.alzzaipo.common.email.domain.Email;
import lombok.Getter;

@Getter
public class SendSignUpEmailVerificationCodeCommand {

    private final Email email;

    public SendSignUpEmailVerificationCodeCommand(String email) {
        this.email = new Email(email);
    }
}
