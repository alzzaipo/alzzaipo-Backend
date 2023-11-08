package com.alzzaipo.member.application.port.out.account.social;

import com.alzzaipo.member.application.port.out.dto.FindSocialAccountCommand;
import com.alzzaipo.member.domain.account.social.SocialAccount;

import java.util.Optional;

public interface FindSocialAccountPort {

    Optional<SocialAccount> findSocialAccount(FindSocialAccountCommand command);
}
