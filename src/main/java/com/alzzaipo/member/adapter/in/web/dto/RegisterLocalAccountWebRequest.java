package com.alzzaipo.member.adapter.in.web.dto;

import com.alzzaipo.common.email.domain.EmailVerificationCode;
import com.alzzaipo.member.application.port.in.dto.RegisterLocalAccountCommand;
import com.alzzaipo.member.domain.account.local.LocalAccountId;
import com.alzzaipo.member.domain.account.local.LocalAccountPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterLocalAccountWebRequest {

    @NotBlank
    @Size(min = 5, max = 20)
    private String accountId;

    @NotBlank
    @Size(min = 8, max = 16)
    private String accountPassword;

    @Email
    private String email;

    @NotBlank
    private String nickname;

    @NotBlank
    private String verificationCode;

    public RegisterLocalAccountCommand toCommand() {
        return new RegisterLocalAccountCommand(
            new LocalAccountId(accountId),
            new LocalAccountPassword(accountPassword),
            new com.alzzaipo.common.email.domain.Email(email),
            nickname,
            new EmailVerificationCode(verificationCode));
    }
}
