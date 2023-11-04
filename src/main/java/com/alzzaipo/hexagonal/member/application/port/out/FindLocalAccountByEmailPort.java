package com.alzzaipo.hexagonal.member.application.port.out;

import com.alzzaipo.hexagonal.common.Email;
import com.alzzaipo.hexagonal.member.application.port.out.dto.SecureLocalAccount;

import java.util.Optional;

public interface FindLocalAccountByEmailPort {

    Optional<SecureLocalAccount> findLocalAccountByEmailPort(Email email);
}
