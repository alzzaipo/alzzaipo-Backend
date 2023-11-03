package com.alzzaipo.hexagonal.member.application.port.out;

import com.alzzaipo.hexagonal.common.Email;
import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.member.domain.LocalAccount.LocalAccountId;
import lombok.Getter;

@Getter
public class SecureLocalAccount {

    private final Uid memberUID;
    private final LocalAccountId accountId;
    private final String encryptedAccountPassword;
    private final Email email;

    public SecureLocalAccount(Uid memberUID, LocalAccountId accountId, String encryptedAccountPassword, Email email) {
        this.memberUID = memberUID;
        this.accountId = accountId;
        this.encryptedAccountPassword = encryptedAccountPassword;
        this.email = email;
    }
}
