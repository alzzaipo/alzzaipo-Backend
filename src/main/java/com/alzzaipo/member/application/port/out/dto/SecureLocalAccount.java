package com.alzzaipo.member.application.port.out.dto;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.Id;
import com.alzzaipo.member.domain.account.local.LocalAccountId;
import lombok.Getter;

@Getter
public class SecureLocalAccount {

    private final Id memberId;
    private final LocalAccountId accountId;
    private final String encryptedAccountPassword;
    private final Email email;

    public SecureLocalAccount(Id memberId, LocalAccountId accountId, String encryptedAccountPassword, Email email) {
        this.memberId = memberId;
        this.accountId = accountId;
        this.encryptedAccountPassword = encryptedAccountPassword;
        this.email = email;
    }
}
