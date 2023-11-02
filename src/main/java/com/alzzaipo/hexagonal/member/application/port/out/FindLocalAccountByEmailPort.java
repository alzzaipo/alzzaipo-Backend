package com.alzzaipo.hexagonal.member.application.port.out;

import com.alzzaipo.domain.account.local.LocalAccount;
import com.alzzaipo.hexagonal.common.Email;

import java.util.Optional;

public interface FindLocalAccountByEmailPort {

    Optional<LocalAccount> findLocalAccountByEmailPort(Email email);
}
