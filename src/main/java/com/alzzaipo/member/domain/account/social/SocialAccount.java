package com.alzzaipo.member.domain.account.social;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.LoginType;
import com.alzzaipo.common.Id;
import lombok.Getter;

@Getter
public class SocialAccount {

    private final Id memberId;
    private final Email email;
    private final LoginType loginType;

    public SocialAccount(Id memberId, Email email, LoginType loginType) {
        this.memberId = memberId;
        this.email = email;
        this.loginType = loginType;
    }
}
