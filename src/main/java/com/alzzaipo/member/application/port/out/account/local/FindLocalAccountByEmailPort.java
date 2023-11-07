package com.alzzaipo.member.application.port.out.account.local;

import com.alzzaipo.common.Email;
import com.alzzaipo.member.application.port.out.dto.SecureLocalAccount;

import java.util.Optional;

public interface FindLocalAccountByEmailPort {

    Optional<SecureLocalAccount> findLocalAccountByEmailPort(Email email);
}
