package com.alzzaipo.member.domain.account.social;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.LoginType;
import com.alzzaipo.common.Uid;
import lombok.Getter;

@Getter
public class SocialAccount {

    private final Uid memberUID;
    private final Email email;
    private final LoginType loginType;

    public SocialAccount(Uid memberUID, Email email, LoginType loginType) {
        this.memberUID = memberUID;
        this.email = email;
        this.loginType = loginType;
    }
}
