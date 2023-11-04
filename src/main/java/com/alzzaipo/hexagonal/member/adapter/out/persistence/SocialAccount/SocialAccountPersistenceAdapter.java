package com.alzzaipo.hexagonal.member.adapter.out.persistence.SocialAccount;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SocialAccountPersistenceAdapter {

    private final NewSocialAccountRepository socialAccountRepository;
}
