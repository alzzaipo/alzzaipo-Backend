package com.alzzaipo.hexagonal.member.application.port.out.account.social;

import com.alzzaipo.hexagonal.member.domain.account.social.SocialAccount;

public interface RegisterSocialAccountPort {

    boolean registerSocialAccount(SocialAccount socialAccount);
}
