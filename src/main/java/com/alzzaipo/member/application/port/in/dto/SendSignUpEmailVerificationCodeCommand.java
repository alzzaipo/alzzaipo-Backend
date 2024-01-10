package com.alzzaipo.member.application.port.in.dto;

import com.alzzaipo.common.email.domain.Email;
import jakarta.validation.Valid;
import lombok.Getter;

@Getter
public class SendSignUpEmailVerificationCodeCommand {

    @Valid
    private final Email email;

    public SendSignUpEmailVerificationCodeCommand(String email) {
        this.email = new Email(email);
    }
}
