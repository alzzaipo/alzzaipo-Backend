package com.alzzaipo.member.application.port.out.account.social;

import com.alzzaipo.common.LoginType;
import com.alzzaipo.common.Id;
import com.alzzaipo.member.domain.account.social.SocialAccount;

import java.util.Optional;

public interface FindSocialAccountByLoginTypePort {

    Optional<SocialAccount> findSocialAccountByLoginType(Id memberId, LoginType loginType);
}
