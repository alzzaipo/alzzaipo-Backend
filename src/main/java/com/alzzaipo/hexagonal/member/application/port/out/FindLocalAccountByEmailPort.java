package com.alzzaipo.hexagonal.member.application.port.out;

import com.alzzaipo.hexagonal.common.Email;
import com.alzzaipo.hexagonal.member.domain.LocalAccount.LocalAccount;

import java.util.Optional;

public interface FindLocalAccountByEmailPort {

    Optional<LocalAccount> findLocalAccountByEmailPort(Email email);
}
