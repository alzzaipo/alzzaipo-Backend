package com.alzzaipo.hexagonal.member.application.port.out.account.local;

import com.alzzaipo.hexagonal.member.application.port.out.dto.SecureLocalAccount;
import com.alzzaipo.hexagonal.member.domain.account.local.LocalAccountId;

import java.util.Optional;

public interface FindLocalAccountByAccountIdPort {

    Optional<SecureLocalAccount> findLocalAccountByAccountId(LocalAccountId localAccountId);
}
