package com.alzzaipo.notification.adapter.in.web.dto;

import com.alzzaipo.common.Id;
import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.email.domain.EmailVerificationCode;
import com.alzzaipo.notification.application.port.dto.ChangeNotificationEmailCommand;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateNotificationEmailRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String verificationCode;

    public ChangeNotificationEmailCommand toCommand(Id memberId) {
        return new ChangeNotificationEmailCommand(
            memberId,
            new Email(email),
            new EmailVerificationCode(verificationCode));
    }
}
