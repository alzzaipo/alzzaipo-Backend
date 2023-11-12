package com.alzzaipo.member.application.port.out.account.social;

import com.alzzaipo.common.LoginType;
import com.alzzaipo.common.Uid;

public interface DeleteSocialAccountPort {

    void deleteSocialAccount(Uid memberUID, LoginType loginType);
}
