package com.alzzaipo.member.application.port.out.account.social;

import com.alzzaipo.common.LoginType;
import com.alzzaipo.common.Id;

public interface DeleteSocialAccountPort {

    void deleteSocialAccount(Id memberId, LoginType loginType);
}
