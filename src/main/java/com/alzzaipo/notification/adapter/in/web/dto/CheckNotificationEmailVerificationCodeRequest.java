package com.alzzaipo.notification.adapter.in.web.dto;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.email.domain.EmailVerificationCode;
import com.alzzaipo.notification.application.port.dto.CheckNotificationEmailVerificationCodeCommand;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CheckNotificationEmailVerificationCodeRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String verificationCode;

    public CheckNotificationEmailVerificationCodeCommand toCommand() {
        return new CheckNotificationEmailVerificationCodeCommand(
            new Email(email),
            new EmailVerificationCode(verificationCode));
    }
}
