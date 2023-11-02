package com.alzzaipo.hexagonal.email.application.port.out;

import com.alzzaipo.hexagonal.email.domain.Email;

import java.util.Optional;

public interface FindEmailVerificationCodePort {

    Optional<String> findEmailVerificationCode(Email email);
}
