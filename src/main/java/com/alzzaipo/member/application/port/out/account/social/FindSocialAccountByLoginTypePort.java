package com.alzzaipo.member.application.port.out.account.social;

import com.alzzaipo.common.LoginType;
import com.alzzaipo.common.Uid;
import com.alzzaipo.member.domain.account.social.SocialAccount;

import java.util.Optional;

public interface FindSocialAccountByLoginTypePort {

    Optional<SocialAccount> findSocialAccountByLoginType(Uid memberUID, LoginType loginType);
}
