package com.alzzaipo.hexagonal.member.adapter.out.persistence.LocalAccount;

import com.alzzaipo.hexagonal.common.Email;
import com.alzzaipo.hexagonal.member.application.port.out.FindLocalAccountByAccountIdPort;
import com.alzzaipo.hexagonal.member.application.port.out.FindLocalAccountByEmailPort;
import com.alzzaipo.hexagonal.member.domain.LocalAccount.LocalAccount;
import com.alzzaipo.hexagonal.member.domain.LocalAccount.LocalAccountId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LocalAccountPersistenceAdapter implements
        FindLocalAccountByAccountIdPort,
        FindLocalAccountByEmailPort
{

    private final NewLocalAccountRepository localAccountRepository;

    @Override
    public Optional<LocalAccount> findLocalAccountByAccountId(LocalAccountId localAccountId) {
        return localAccountRepository.findByAccountId(localAccountId.get());
    }

    @Override
    public Optional<LocalAccount> findLocalAccountByEmailPort(Email email) {
        return localAccountRepository.findByEmail(email.get());
    }
}
