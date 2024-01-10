package com.alzzaipo.member.application.port.in.dto;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.email.domain.EmailVerificationCode;
import com.alzzaipo.member.adapter.in.web.dto.RegisterLocalAccountWebRequest;
import com.alzzaipo.member.domain.account.local.LocalAccountId;
import com.alzzaipo.member.domain.account.local.LocalAccountPassword;
import lombok.Getter;

@Getter
public class RegisterLocalAccountCommand {

    private final LocalAccountId localAccountId;
    private final LocalAccountPassword localAccountPassword;
    private final Email email;
    private final String nickname;
    private final EmailVerificationCode emailVerificationCode;

    public RegisterLocalAccountCommand(String accountId, String accountPassword, String email, String nickname,
        String emailVerificationCode) {
        this.localAccountId = new LocalAccountId(accountId);
        this.localAccountPassword = new LocalAccountPassword(accountPassword);
        this.email = new Email(email);
        this.nickname = nickname;
        this.emailVerificationCode = new EmailVerificationCode(emailVerificationCode);
    }

    public static RegisterLocalAccountCommand build(RegisterLocalAccountWebRequest dto) {
        return new RegisterLocalAccountCommand(
            dto.getAccountId(),
            dto.getAccountPassword(),
            dto.getEmail(),
            dto.getNickname(),
            dto.getVerificationCode());
    }
}
