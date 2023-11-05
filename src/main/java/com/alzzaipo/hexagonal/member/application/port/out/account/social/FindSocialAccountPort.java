package com.alzzaipo.hexagonal.member.application.port.out.account.social;

import com.alzzaipo.hexagonal.member.application.port.out.dto.FindSocialAccountCommand;
import com.alzzaipo.hexagonal.member.domain.account.social.SocialAccount;

import java.util.Optional;

public interface FindSocialAccountPort {

    Optional<SocialAccount> findSocialAccount(FindSocialAccountCommand command);
}
