package com.alzzaipo.hexagonal.member.application.port.out;

import com.alzzaipo.hexagonal.member.domain.SocialAccount.SocialAccount;

public interface RegisterSocialAccountPort {

    boolean registerSocialAccount(SocialAccount socialAccount);
}
