package com.alzzaipo.hexagonal.member.domain.SocialAccount;

import com.alzzaipo.hexagonal.common.Email;
import com.alzzaipo.hexagonal.common.LoginType;
import com.alzzaipo.hexagonal.common.Uid;
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
