package com.alzzaipo.member.application.port.out.account.local;

import com.alzzaipo.member.application.port.out.dto.SecureLocalAccount;
import com.alzzaipo.member.domain.account.local.LocalAccountId;

import java.util.Optional;

public interface FindLocalAccountByIdPort {

    Optional<SecureLocalAccount> findLocalAccountById(LocalAccountId localAccountId);
}
