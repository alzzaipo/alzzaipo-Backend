package com.alzzaipo.member.application.port.out.account.social;

import com.alzzaipo.member.domain.account.social.SocialAccount;

public interface RegisterSocialAccountPort {

    boolean registerSocialAccount(SocialAccount socialAccount);
}
