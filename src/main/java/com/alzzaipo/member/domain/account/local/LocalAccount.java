package com.alzzaipo.member.domain.account.local;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.Id;
import lombok.Getter;

@Getter
public class LocalAccount {

    private final Id memberId;
    private final LocalAccountId accountId;
    private final LocalAccountPassword accountPassword;
    private final Email email;

    public LocalAccount(Id memberId, LocalAccountId accountId, LocalAccountPassword accountPassword, Email email) {
        this.memberId = memberId;
        this.accountId = accountId;
        this.accountPassword = accountPassword;
        this.email = email;
    }
}
