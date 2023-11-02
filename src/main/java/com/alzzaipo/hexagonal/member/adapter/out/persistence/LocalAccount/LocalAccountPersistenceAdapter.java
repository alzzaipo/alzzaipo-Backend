package com.alzzaipo.hexagonal.member.adapter.out.persistence.LocalAccount;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocalAccountPersistenceAdapter {

    private final NewLocalAccountRepository localAccountRepository;
}
