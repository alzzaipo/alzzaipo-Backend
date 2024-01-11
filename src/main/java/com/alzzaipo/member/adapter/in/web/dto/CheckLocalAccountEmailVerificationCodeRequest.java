package com.alzzaipo.member.adapter.in.web.dto;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.email.domain.EmailVerificationCode;
import com.alzzaipo.common.email.domain.EmailVerificationPurpose;
import com.alzzaipo.member.application.port.in.dto.CheckLocalAccountEmailVerificationCodeCommand;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CheckLocalAccountEmailVerificationCodeRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String verificationCode;

    public CheckLocalAccountEmailVerificationCodeCommand toCommand(EmailVerificationPurpose emailVerificationPurpose) {
        return new CheckLocalAccountEmailVerificationCodeCommand(
            new Email(email),
            new EmailVerificationCode(verificationCode),
            emailVerificationPurpose);
    }
}
