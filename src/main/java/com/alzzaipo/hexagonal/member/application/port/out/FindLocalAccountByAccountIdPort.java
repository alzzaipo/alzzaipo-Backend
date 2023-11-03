package com.alzzaipo.hexagonal.member.application.port.out;

import com.alzzaipo.hexagonal.member.domain.LocalAccount.LocalAccountId;

import java.util.Optional;

public interface FindLocalAccountByAccountIdPort {

    Optional<SecureLocalAccount> findLocalAccountByAccountId(LocalAccountId localAccountId);
}
