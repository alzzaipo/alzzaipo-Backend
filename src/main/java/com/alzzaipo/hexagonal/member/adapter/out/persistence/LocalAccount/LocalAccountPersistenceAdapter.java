package com.alzzaipo.hexagonal.member.adapter.out.persistence.LocalAccount;

import com.alzzaipo.domain.account.local.LocalAccount;
import com.alzzaipo.hexagonal.member.application.port.out.FindLocalAccountByAccountIdPort;
import com.alzzaipo.hexagonal.member.domain.LocalAccount.LocalAccountId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LocalAccountPersistenceAdapter implements FindLocalAccountByAccountIdPort {

    private final NewLocalAccountRepository localAccountRepository;

    @Override
    public Optional<LocalAccount> findLocalAccountByAccountId(LocalAccountId localAccountId) {
        return localAccountRepository.findByAccountId(localAccountId.get());
    }
}
